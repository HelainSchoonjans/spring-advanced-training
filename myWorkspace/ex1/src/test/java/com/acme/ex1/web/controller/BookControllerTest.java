package com.acme.ex1.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
	private MockMvc mockMvc;
	
	@Test
	void getBooks() throws Exception {
		this.mockMvc.perform(
				get("/books")
				)
		.andExpect(model().attributeExists("probe"))
		.andExpect(model().attributeDoesNotExist("results"))
		.andExpect(view().name("books/list"));
	}
	
	@Test
	void getBooksWithResults() throws Exception {
		this.mockMvc.perform(
				get("/books")
				.param("title", "a")
				)
		.andExpect(model().attributeExists("probe", "results"))
		.andExpect(view().name("books/list"));
	}
	
	
	@Test
	void getBook() throws Exception {
		this.mockMvc.perform(
				get("/books/1")
				)
		.andExpect(model().attributeExists("entity", "command"))
		.andExpect(view().name("books/detail"));
	}
	
	@Test
	void getBookNotFound() throws Exception {
		this.mockMvc.perform(
				get("/books/128")
				)
		.andExpect(model().attributeDoesNotExist("entity"))
		.andExpect(view().name("books/detail"));
	}
}