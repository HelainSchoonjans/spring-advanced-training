package com.acme.ex4;

public class ReservationRow {

	private int bookId;
	private String username;
	private String bookTitle;

	public ReservationRow(int bookId, String username, String bookTitle) {
		super();
		this.bookId = bookId;
		this.username = username;
		this.bookTitle = bookTitle;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

}
