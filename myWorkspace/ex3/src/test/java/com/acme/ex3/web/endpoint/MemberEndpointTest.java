package com.acme.ex3.web.endpoint;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureWebTestClient
class MemberEndpointTest {


	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ObjectMapper jackson;

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Test 
	//@Sql(statements = {"delete from Member_Category", "delete from Member where id>1"})
	void postMembers() throws Exception {
		Map<String, Object> body = Map.of(
				"firstname", "Jane",
				"lastname","Doe",
				"username","jane.doe"+Instant.now().toEpochMilli(),
				"password","azerty"
			);
				
		this.webTestClient.post()
			.uri("/members")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jackson.writeValueAsString(body))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isCreated();
	}
	
	@Test 
	void postMembersWithInvalidUsername() throws Exception {
		Map<String, Object> body = Map.of(
			"firstname", "Jane",
			"lastname","Doe",
			"username", "",
			"password","azerty"
		);
		
		this.webTestClient.post()
			.uri("/members")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jackson.writeValueAsString(body))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest() // because username is empty
			.expectBody().jsonPath("$.cause", "validation");
	}
	
	@Test 
	void postMembersWithDuplicateUsername() throws Exception {
		Map<String, Object> body = Map.of(
			"firstname", "Jane",
			"lastname","Doe",
			"username", "jdoe",
			"password","azerty"
		);
		
		this.webTestClient.post()
			.uri("/members")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jackson.writeValueAsString(body))
			.accept(MediaType.APPLICATION_JSON)
			.header("accept-language", "fr")
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody().jsonPath("$.data", messageSource.getMessage("memberRegistration-account.already.exists", null, Locale.FRANCE)); // because username is duplicate
	}
}
