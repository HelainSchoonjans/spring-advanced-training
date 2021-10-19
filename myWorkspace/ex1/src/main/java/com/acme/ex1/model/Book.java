package com.acme.ex1.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity // suppose une table Book possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
public class Book extends AbstractPersistentEntity<Integer> {

    // et une colonne title
    private String title;

    @ManyToOne // et une colonne author_id
    private Author author;

    @ManyToOne // et une colonne category_id
    private Category category;

    @ElementCollection // et une table Book_comments possédant une colonne book_id
    private List<Comment> comments;

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
