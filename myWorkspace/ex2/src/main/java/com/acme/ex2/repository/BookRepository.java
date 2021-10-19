package com.acme.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
