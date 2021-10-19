package com.acme.ex2.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@SpringBootTest
@AutoConfigureMockMvc
class ReservationRestEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    
    private ObjectMapper jackson = new ObjectMapper();

    @Test
    void testReservations401() throws Exception {
        ObjectNode node = jackson.createObjectNode()
                .put("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("returnDate", LocalDate.now().plusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("bookId", 1)
                .put("username", "jdoe");

        mockMvc.perform(post("/reservations")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(node.toString()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
    
    @Test
    void testReservations400() throws Exception {
        ObjectNode node = jackson.createObjectNode()
                .put("pickupDate", LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("returnDate", LocalDate.now().minusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("bookId", 1)
                .put("username", "jdoe");

        mockMvc.perform(post("/reservations")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    //.header("Authorization", "Bearer " + getToken())
                    .content(node.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }    

    @Test
    @Sql(statements = "delete from Reservation")
    void testReservations201() throws Exception {

        ObjectNode node = jackson.createObjectNode()
                .put("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("returnDate", LocalDate.now().plusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("bookId", 1)
                .put("username", "jdoe");

        mockMvc.perform(post("/reservations")
                    .accept(MediaType.APPLICATION_JSON)
                    //.header("Authorization", "Bearer " + getToken())
                    .contentType(MediaType.APPLICATION_JSON).content(node.toString()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    private String getToken() throws Exception {

        String tokenEndpoint = jackson.readTree(new URL(this.issuer + "/.well-known/openid-configuration")).get("token_endpoint").asText();

        RestTemplate restTemplate = new RestTemplate();

        String formData = Map.of(
                "client_id", "ex1-frontend", 
                "username", "jdoe", 
                "password", "azerty", 
                "scope", "openid",
                "grant_type", "password"
                )
                .entrySet()
                .stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "="+ URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        RequestEntity<String> requestEntity = RequestEntity.post(URI.create(tokenEndpoint))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(formData);

        ResponseEntity<JsonNode> exchange = restTemplate.exchange(requestEntity, JsonNode.class);
        String id_token = exchange.getBody().get("id_token").asText();
        return id_token;
    }
    
}
