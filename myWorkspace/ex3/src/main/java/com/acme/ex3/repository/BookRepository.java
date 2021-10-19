package com.acme.ex3.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.acme.ex3.model.Book;

import reactor.core.publisher.Flux;

public interface BookRepository extends R2dbcRepository<Book, Integer> {

	Flux<Book> findByTitleContaining(String title);
}
