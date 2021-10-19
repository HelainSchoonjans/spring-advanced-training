import { HttpClient, json } from 'aurelia-fetch-client';
import { autoinject } from 'aurelia-framework';

@autoinject
export class Detail {

    book: {
        title: string,
        comments: Array<{ text: string }>,
        _embedded: {
            author: { name: string }
        }
        _links: {
            self: { href: string }
        }
    };

    command: {
        pickupDate: Date
        returnDate: Date
    }
    commandResult: {
        status: number
        error?: {
            description: string,
            data: any
        }
    }

    constructor(private httpClient: HttpClient) {
    }

    async activate(routeParams: { id: number }) {

        this.book = await this.httpClient.get(`/books/${routeParams.id}`).then(x => x.json());
    }

    async borrow() {
        this.commandResult = null;

        const resp = await this.httpClient.post("/reservations", json({ ...this.command, book: this.book._links.self.href }));
        this.commandResult = { status: resp.status };
        if (resp.status == 201) {
            // ???
        }
        if (resp.status == 400) {
            this.commandResult.error = await resp.json();
        }
    }
}
