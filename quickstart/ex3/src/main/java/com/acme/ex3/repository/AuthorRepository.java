package com.acme.ex3.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.acme.ex3.model.Author;

public interface AuthorRepository extends R2dbcRepository<Author, Integer> {
}
