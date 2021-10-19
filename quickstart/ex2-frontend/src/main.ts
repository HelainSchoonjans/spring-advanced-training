import { Aurelia, PLATFORM } from 'aurelia-framework';

export function configure(aurelia: Aurelia): Promise<Aurelia> {
  aurelia.use
    .standardConfiguration();

  aurelia.use.developmentLogging();

  return aurelia.start()
    .then(aurealia => aurealia.setRoot(PLATFORM.moduleName('app')))
}

export type Config = {
  apiEndpoint: string,
  oidc: {
    redirect_uri: string,
    scope: string
    client_id: string,
    usenonce: boolean,
    response_mode: string,
    endpoints: {
      login: string,
      logout: string,
      token: string,
      userinfo: string
    }
  }
}

export class Authentication {
  sub?: string
  profile?: {
    given_name: string,
    family_name: string,
    email: string
  }
  tokens: {
    id_token: string // a JWT
    access_token: string // to communicate with IDP on the behalf of the user. Can be opaque
    refresh_token: string // may be null
    expires_in: number
  }

  get anonymous() {
    return this.profile == null
  }

  clear() {
    this.sub = null;
    this.profile = null;
    this.tokens = null;
  }
}

declare const __CONFIG__: Config;

export const settings: Config = __CONFIG__;
