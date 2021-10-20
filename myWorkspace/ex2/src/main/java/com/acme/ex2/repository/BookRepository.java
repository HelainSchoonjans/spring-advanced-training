package com.acme.ex2.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Book;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = BookRepository.BookProjection.class)
public interface BookRepository extends JpaRepository<Book, Integer> {

    // accessible on /books/search/byTitleAndAuthor
    // test with http://localhost:8080/books/search/byTitleAndAuthor?title=Walden&firstname=Henry
    @RestResource(path = "byTitleAndAuthor")
    List<Book> findByTitleContainingAndAuthorFirstnameContaining(
            @Param("title") String title, @Param("firstname") String firstname);

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
