package com.acme.ex2.repository;

import org.springframework.beans.factory.annotation.Value;
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

    // to prevent deletions with rest resources:
    /*@Override
    @RestResource(exported = false) // consequence: status 405 si delete sur /books/{id}
    void deleteById(Integer integer);
     */

    interface BookProjection {

        Long getId();

        String getTitle();

        @Value("#{target.author.firstname} #{target.author.lastname}")
        String getAuthor();

        @Value("#{target.category.name}")
        String getCategoryName();
    }
}
