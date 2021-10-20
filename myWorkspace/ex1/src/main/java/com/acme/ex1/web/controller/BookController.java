package com.acme.ex1.web.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.acme.ex1.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.acme.ex1.model.Book;
import com.acme.ex1.service.command.ReservationCommand;


@Controller
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("books")
	String list(Map<String, Object> model) {
		model.put("probe", new Book());
		return "books/list";
	}

	@GetMapping(path = "books", params = "title")
	String list(@ModelAttribute("probe") Book probe, Map<String, Object> model) {
		List<Book> results = bookRepository.findAll(Example.of(probe));
		
		model.put("results", results);
		return "books/list";
	}

	@GetMapping("books/{id}")
	String book(@PathVariable int id, Map<String, Object> model) {
		Optional<Book> maybeBook = bookRepository.findById(id);
		maybeBook.ifPresent(b -> model.put("entity", b));
		
		model.putIfAbsent("command", new ReservationCommand());
		return "books/detail";
	}
	
}

