import { PLATFORM, autoinject, LogManager } from 'aurelia-framework';
import { RouterConfiguration } from 'aurelia-router';
import { HttpClient } from 'aurelia-fetch-client';
import { Logger } from 'aurelia-logging';
import { settings, Authentication } from './main';


@autoinject
export class App {

  logger: Logger = LogManager.getLogger("app");

  constructor(private httpClient: HttpClient, private authentication: Authentication) {

    this.httpClient.configure(config => {
      config.withBaseUrl(settings.apiEndpoint)
        .withDefaults({
          headers: { "accept": "application/json" }
        })
        .interceptors = [{
          request(req) {
            if (window.fetch != window["f"]) {
              // compromission !
              this.logger.warn("xss detected !!")
              window.fetch = window["f"];
            }
            return req;
          }
        }]
    });
  }

  configureRouter(config: RouterConfiguration) {
    config.options.pushState = true;
    config.options.hashChange = false;

    config.map([
      { route: ['', 'books'], name: 'home', moduleId: PLATFORM.moduleName('./book/list') },
      { route: ['books/:id'], moduleId: PLATFORM.moduleName('./book/detail') }
    ]);
  }

  signIn(prompt: 'login' | 'none' = 'login') {
    console.log("sign in")
    // https://stackoverflow.com/a/2117523
    const uuidv4 = () => {
      return ('' + [1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, ch => {
        let c = Number(ch);
        return (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
      }
      )
    }

    const stateObj = {
      currentLocation: encodeURIComponent(window.location.pathname),
      nonce: uuidv4()
    };

    const params = {
      client_id: settings.oidc.client_id,
      redirect_uri: settings.oidc.redirect_uri,
      response_mode: settings.oidc.response_mode,
      response_type: "code",
      scope: "openid",
      prompt: prompt,
      state: window.btoa(JSON.stringify(stateObj)),
      nonce: uuidv4()
    }

    localStorage.setItem("state", params.state);
    localStorage.setItem("nonce", params.nonce);

    const querystring = new URLSearchParams(params).toString();
    //this.router.navigate(`${settings.oidc.endpoints.login}?${querystring}`);
    window.location.href = `${settings.oidc.endpoints.login}?${querystring}`;
  }

  async signOut() {
    const body = `client_id=${settings.oidc.client_id}&refresh_token=${this.authentication.tokens.refresh_token}`;

    const headers = new Headers({
      "accept": "application/json",
      "content-type": "application/x-www-form-urlencoded"
    });

    await fetch(settings.oidc.endpoints.logout, { method: "POST", body: body, headers: headers })

    localStorage.removeItem("silent-login");

    this.authentication.clear();
    this.httpClient.configure(config => {
      const headers = config.defaults.headers;
      delete headers["Authorization"];
    });
  }

  async activate() {
    const params = (() => {
      switch (settings.oidc.response_mode) {
        case "fragment":
          return window.location.hash.substring(1)
            .split("&")
            .map(x => x.split("="))
            .reduce((acc, param) => ({ ...acc, [param[0]]: param[1] }), { state: null, code: null });
        case "query": {
          const urlSearchParams = new URLSearchParams(window.location.search);
          return { code: urlSearchParams.get("code"), state: urlSearchParams.get("state") }
        }
        default: return { code: null, state: null }
      }
    })();

    if (!params.state || !params.code) {
      if (localStorage.getItem("silent-login")) {
        this.signIn('none');
      }
      else {
        return;
      }
    }
    const state = decodeURIComponent(params.state);
    if (state != localStorage.getItem("state")) {
      this.logger.error("unknown state");
      return;
    }
    this.logger.debug("state is valid");
    const stateObj = JSON.parse(window.atob(state));
    localStorage.removeItem("state");

    const code = params.code;
    this.logger.debug("got code : " + code + ", ask for token")

    if (window.fetch != window["f"]) {
      console.log("xss detected !!")
      window.fetch = window["f"];
    }
    const headers = new Headers({
      "accept": "application/json",
      "content-type": "application/x-www-form-urlencoded"
    });

    const body = `client_id=${settings.oidc.client_id}&code=${code}&grant_type=authorization_code&redirect_uri=${settings.oidc.redirect_uri}`;

    this.authentication.tokens = await fetch(settings.oidc.endpoints.token, { method: "POST", body: body, headers: headers }).then(x => x.json());

    localStorage.setItem("silent-login", "true");

    this.logger.debug("token : "+ this.authentication.tokens.id_token);

    const parts = this.authentication.tokens.id_token.split('.');
    const header = parts[0];
    const payload = parts[1];
    const signature = parts[2];
    
    this.logger.debug("header : "+header);
    this.logger.debug("header decoded : "+atob(header));
    this.logger.debug("payload : "+payload);
    this.logger.debug("payload decoded : "+atob(payload));
    this.logger.debug("signature : "+signature);

    const claims: {
      sub: string,
      email: string,
      nonce?: string,
      given_name: string,
      family_name: string
    } = JSON.parse(atob(payload));

    // redirect user
    const path = decodeURIComponent(stateObj.currentLocation);
    this.logger.debug("redirect user to " + path)
    window.history.replaceState(window.history.state, null, path);

    if (claims.nonce != localStorage.getItem("nonce")) {
      this.logger.error("unknown nonce");
      return;
    }
    else {
      this.logger.debug("nonce is valid");
      localStorage.removeItem("nonce");
    }

    const access_token_expires_in = this.authentication.tokens.expires_in;

    this.authentication.sub = claims.sub;

    this.authentication.profile = {
      given_name: claims.given_name,
      family_name: claims.family_name,
      email: claims.email
    };


    // configure http client so further request will have Authorization header 
    this.httpClient.configure(config => {
      config.withDefaults({
        headers: { ...config.defaults.headers, "Authorization": "Bearer " + this.authentication.tokens.id_token }
      });
    });

    if (access_token_expires_in) {
      const expirationInmilleseconds = access_token_expires_in * 1000;
      window.setInterval(async () => {
        const b = `client_id=${settings.oidc.client_id}&grant_type=refresh_token&refresh_token=${this.authentication.tokens.refresh_token}`
        if (window.fetch != window["f"]) {
          console.log("xss detected !!")
          window.fetch = window["f"];
        }
        this.authentication.tokens = await fetch(settings.oidc.endpoints.token, { method: "POST", body: b, headers: headers }).then(x => x.json());
        this.httpClient.configure(config => {
          config.withDefaults({
            headers: { ...config.defaults.headers, "Authorization": "Bearer " + this.authentication.tokens.access_token }
          });
        });
        console.log(this.authentication.tokens)
      }, expirationInmilleseconds)
    }
  }
}
