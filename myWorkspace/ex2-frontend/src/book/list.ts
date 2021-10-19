import { HttpClient, json, } from 'aurelia-fetch-client'
import { autoinject, buildQueryString } from 'aurelia-framework';


@autoinject
export class List {

    criteria: {
        title: string,
        authorName: string
    }

    results: Array<{
        title: string,
        author: string
    }>

    constructor(private httpClient: HttpClient) {
    }

    async activate() {
        await this.find();
    }

    private objectToQueryString(obj: any, propertyOwner: string = null): string {
        if (obj == null) {
            return "";
        }
        return Object.keys(obj).filter(key => obj[key]).map(objKey => {
            const key = propertyOwner ? `${propertyOwner}.${objKey}` : objKey;
            const value = obj[objKey];
            return typeof value === "object" ? this.objectToQueryString(value, key) : `${key}=${encodeURIComponent(value)}`;
        }).join("&");
    } // https://stackoverflow.com/a/39499342/10938834


    async find() {
        const querystring = this.objectToQueryString(this.criteria);

        this.results = (await this.httpClient.get(querystring ? `/books/search/byTitleAndAuthor?${querystring}` : '/books').then(resp => resp.json()))._embedded.books;
    }
}
