package com.acme.ex3.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Reservation {

	@Id
	private Integer id;
	private LocalDate pickupdate;
	private LocalDate returndate;
	private int bookId;
	private int memberId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public LocalDate getPickupdate() {
		return pickupdate;
	}

	public void setPickupdate(LocalDate pickupdate) {
		this.pickupdate = pickupdate;
	}

	public LocalDate getReturndate() {
		return returndate;
	}

	public void setReturndate(LocalDate returndate) {
		this.returndate = returndate;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

}
