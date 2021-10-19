package com.acme.ex1.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity // suppose une table Author possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
public class Author extends AbstractPersistentEntity<Integer> {

    // et une colonne firstname
    private String firstname;

    // et une colonne lastname
    private String lastname;

    @OneToMany(mappedBy = "author")
    private List<Book> publications;

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

    public List<Book> getPublications() {
        return publications;
    }

    public void setPublications(List<Book> publications) {
        this.publications = publications;
    }

    public String getFullname(){
        return this.firstname+" "+this.lastname;
    }
}
