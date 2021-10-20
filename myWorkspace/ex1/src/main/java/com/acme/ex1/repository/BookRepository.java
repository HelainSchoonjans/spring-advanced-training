package com.acme.ex1.repository;

import com.acme.ex1.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByTitleLike(String title);
}
