package com.acme.ex1.web.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.acme.ex1.model.Book;
import com.acme.ex1.service.command.ReservationCommand;


@Controller
public class BookController {

	@GetMapping("books")
	String list(Map<String, Object> model) {
		model.put("probe", new Book());
		return "books/list";
	}
    
	@GetMapping(path = "books", params = "title")
	String list(@ModelAttribute("probe") Book probe, Map<String, Object> model) {
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir les livres correspondant à l'exemple reçu en argument.

		List<Book> results = null;
		
		model.put("results", results);
		return "books/list";
	}

	@GetMapping("books/{id}")
	String book(@PathVariable int id, Map<String, Object> model) {
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir le Book dont l'id est id
		Optional<Book> maybeBook = null;
		maybeBook.ifPresent(b -> model.put("entity", b));
		
		model.putIfAbsent("command", new ReservationCommand());
		return "books/detail";
	}
	
}

