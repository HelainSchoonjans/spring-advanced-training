package com.acme.ex3.web.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@SpringBootTest
@AutoConfigureWebTestClient
class BookEndpointTest {

    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    void getBooks() throws Exception {
        ResponseSpec exchange = this.webTestClient.get()
        	.uri("/books?keyword=Walden")
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
        exchange.expectStatus().isOk();
    }

    @Test
    void getBook() throws Exception {
        ResponseSpec exchange = this.webTestClient.get()
        	.uri("/books/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
        exchange.expectStatus().isOk();
    }

    @Test
    void getBookNotFound() throws Exception {
        ResponseSpec exchange = this.webTestClient.get()
        	.uri("/books/128")
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
        exchange.expectStatus().isNotFound();
    }
}
