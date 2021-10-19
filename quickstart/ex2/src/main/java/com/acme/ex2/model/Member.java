package com.acme.ex2.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;


@Entity // suppose une table Member possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
public class Member extends AbstractPersistentEntity<Integer> {

    // et une colonne firstname
	@NotBlank
    private String firstname;

    // et une colonne lastname
	@NotBlank
    private String lastname;

    @Embedded // et une colonne username et une colonne password pour stocker les propriétés de l'account
    private Account account;

    @ElementCollection // et une table book_comments possédant une colonne member_id
    @CollectionTable(name="book_comments")
    private List<Comment> comments;
    
    @ManyToMany // suppose une table de liaison Member_Category possédant une colonne Member_id et une colonne preferences_id
    private List<Category> preferences;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Category> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Category> preferences) {
        this.preferences = preferences;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}

