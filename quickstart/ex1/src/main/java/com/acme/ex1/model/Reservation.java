package com.acme.ex1.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;

@Entity // suppose une table Reservation possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
public class Reservation extends AbstractPersistentEntity<Integer> {

	@ManyToOne // et une colonne member_id
    private Member member;

	@ManyToOne //et une colonne book_id
    private Book book;

	// et une colonne pickupDate
	@FutureOrPresent
    private LocalDate pickupDate;

    // et une colonne returnDate
	@FutureOrPresent
    private LocalDate returnDate;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
