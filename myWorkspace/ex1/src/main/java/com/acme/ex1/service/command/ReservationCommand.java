package com.acme.ex1.service.command;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.context.SecurityContextHolder;
import com.acme.ex1.model.Reservation;
import com.acme.ex1.service.AbstractCommand;

public class ReservationCommand extends AbstractCommand {

	private static final long serialVersionUID = 1L;

	// inputs
    private int bookId;
    @NotBlank
    private String username;
    @NotNull @FutureOrPresent
    private LocalDate pickupDate;
    @NotNull @Future
    private LocalDate returnDate;
    // output
    private Reservation reservation;

    public ReservationCommand() {
        // we could get the username from the security context instead of passing it around
        // ...GetContext.getUsername()
        this.username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
