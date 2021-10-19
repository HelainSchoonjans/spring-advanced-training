package com.acme.ex1.model;

import java.time.Instant;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Comment {

	@ManyToOne // suppose une colonne member_id
	private Member member;
	
	@ManyToOne // et une colonne book_id
	private Book book;

	// et une colonne text
	private String text;

	// et une colonne date
	private Instant date;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
}
