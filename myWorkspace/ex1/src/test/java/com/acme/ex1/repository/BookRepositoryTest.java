package com.acme.ex1.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;

import com.acme.ex1.model.Author;
import com.acme.ex1.model.Book;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-for-tests.properties")
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /*
    @PostConstruct
    public void cache() {
        // since the categories are cacheable, this will initialise a level 2 cache with all categories
        categoryRepository.findAll();
    }
    Note: by getting entities fom metamodel, we can check for each if cacheable annotation is present and have a list
    */

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testSave() {
        System.out.println("********* entering testSave ***********");
        System.out.println("I am about to use " + authorRepository.toString());
        System.out.println("I am about to use " + repository.toString());
        Author author = authorRepository.getById(1); // equivalent of entityManager.getReference(Author.class, 1))
        Book b = new Book();
        b.setTitle("new book");
        b.setAuthor(author);

        // equivalent with entityManager.persist(b);
        repository.save(b);

        assertNotNull(b.getId());
        System.out.println("********* exiting testSave ***********");
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testUpdateExisting() {
        Optional<Book> _book = repository.findById(1);
        if (_book.isPresent()) {
            Book book = _book.get();
            book.setTitle("new title");
        }
        // we can see in the logs that at the end of the transaction we flush the state and save the changes to book
        // jpa makes an invisible update
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testDelete() {
        // equivalent of em.remove(em.find(Book.class, 1))
        repository.deleteById(1);

        assertTrue(repository.findById(1).isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testFindById() {
        Optional<Book> _book1 = repository.findById(1); // équivalent de Optional.ofNullable(entityManager.find(Book.class, 1))
        System.out.println("after first call to getById");
        Book book1 = _book1.get();

        Optional<Book> _book1Again = repository.findById(1); // équivalent de Optional.ofNullable(entityManager.find(Book.class, 1))
        System.out.println("after second call to getById");
        Book book1Again = _book1Again.get();

        assertSame(book1, book1Again);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testFindByIdWithLazyLoading() {
        // equivalent of Optional.ofNullable(entityManager.find(Book.class, 1))
        Optional<Book> _book = repository.findById(1);
        System.out.println("after call to getById");
        _book.ifPresent(b -> {
            System.out.println(b.getTitle());
            System.out.println(b.getAuthor().getFullname());
            System.out.println(b.getComments().size());
        });
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testFindAll() {
        Book probe = new Book();
        probe.setTitle("e");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // PAS EN PROD !!!
        Example<Book> example = Example.of(probe, matcher);

        List<Book> results = repository.findAll(example);
        for (Book book : results) {
            System.out.println(String.format("%-40s %-40s %-40s", book.getTitle(), book.getAuthor().getFullname(), book.getCategory().getName()));
        }
    }
}
