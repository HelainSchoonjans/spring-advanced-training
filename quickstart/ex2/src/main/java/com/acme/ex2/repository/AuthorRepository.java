package com.acme.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
