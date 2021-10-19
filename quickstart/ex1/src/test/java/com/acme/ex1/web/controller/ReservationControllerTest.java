package com.acme.ex1.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
	private MockMvc mockMvc;
	
	@Test
	void postReservationsAsAnonymousUser() throws Exception {
        this.mockMvc.perform(
                post("/reservations")
                //.with(csrf())
                .param("bookId", "1")
                .param("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .param("returnDate", LocalDate.now().plusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                )
        .andExpect(redirectedUrl("http://localhost/login"));
    }
	
    @Test
    void postReservationsAsAuthenticatedUserWithInsufficientPermissions() throws Exception {
        this.mockMvc.perform(
                post("/reservations")
                //.with(csrf())
                .param("bookId", "1")
                .param("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .param("returnDate", LocalDate.now().plusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                )
        .andExpect(status().isForbidden());
    }   
    
    @Sql(statements = "delete from reservation")
    @Test
    void postReservationsAsAuthenticatedUser() throws Exception {
        this.mockMvc.perform(
                post("/reservations")
                //.with(csrf())
                .param("bookId", "1")
                .param("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .param("returnDate", LocalDate.now().plusDays(10).format(DateTimeFormatter.ISO_LOCAL_DATE))
                )
        .andExpect(redirectedUrl("/books"));
    }
    
    @Test
    void postReservationsAsAuthenticatedUserWithInvalidInput() throws Exception {
        this.mockMvc.perform(
                post("/reservations")
                //.with(csrf())
                .param("bookId", "1")
                .param("pickupDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                .param("returnDate", LocalDate.now().plusDays(-10).format(DateTimeFormatter.ISO_LOCAL_DATE))

                )
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/books/1"))
        .andExpect(flash().attributeExists("command"))
        .andExpect(flash().attributeExists(BindingResult.MODEL_KEY_PREFIX+ "command"));
    }
}