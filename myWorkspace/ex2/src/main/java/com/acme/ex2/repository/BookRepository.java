package com.acme.ex2.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Book;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = BookRepository.BookProjection.class)
public interface BookRepository extends JpaRepository<Book, Integer> {

    interface BookProjection {

        @Value("#{target.id}")
        Long getId();
        @Value("#{target.title}")
        String getTitle();
        @Value("#{target.author.firstname} #{target.author.lastname}")
        String getAuthorName();
        @Value("#{target.category.name}")
        String getCategoryName();
    }
}
