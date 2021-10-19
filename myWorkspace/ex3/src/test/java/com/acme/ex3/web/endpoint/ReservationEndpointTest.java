package com.acme.ex3.web.endpoint;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.acme.ex3.service.command.ReservationCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@SpringBootTest
@AutoConfigureWebTestClient
class ReservationEndpointTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ObjectMapper jackson;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Test
	@WithMockUser(username = "jdoe")
	//@Sql(statements = {"delete from Reservation"})
	void postReservations() throws Exception {
		Map<String, Object> body = Map.of(
			"bookId", 1,
			"pickupDate", LocalDate.now().plusDays(1),
			"returnDate", LocalDate.now().plusDays(10)
		);

		//this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("jdoe"))).post()
		this.webTestClient.post()
			.uri("/reservations")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jackson.writeValueAsString(body))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().exists("location");
	}

	@Test
	@WithMockUser(username = "jdoe")
	void postReservationsWithInvalidReturnDate() throws Exception {
		Map<String, Object> body = Map.of(
			"bookId",1,
			"pickupDate", LocalDate.now().plusDays(1),
			"returnDate", LocalDate.now().plusDays(-10)
		);

		String json = jackson.writeValueAsString(body);
		ReservationCommand cmd = jackson.readValue(json, ReservationCommand.class);

		//this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("jdoe"))).post()
		this.webTestClient.post()
			.uri("/reservations")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jackson.writeValueAsString(body))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Test 
	@WithMockUser(username = "gdoe")
	void postReservationsWithUnknownUsername() throws Exception {

		Map<String, Object> body = Map.of(
				"bookId", 1,
				"pickupDate", LocalDate.now().plusDays(1),
				"returnDate", LocalDate.now().plusDays(10)
		);

		//this.webTestClient.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("jdoe"))).post()
		this.webTestClient.post()
				.uri("/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(jackson.writeValueAsString(body))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody().jsonPath("$.data").isEqualTo(messageSource.getMessage("reservation-member.not.found", null, Locale.getDefault()));
	}
}
