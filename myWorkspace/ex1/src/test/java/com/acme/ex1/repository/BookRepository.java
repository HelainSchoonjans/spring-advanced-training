package com.acme.ex1.repository;

import com.acme.ex1.model.Author;
import com.acme.ex1.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
}
