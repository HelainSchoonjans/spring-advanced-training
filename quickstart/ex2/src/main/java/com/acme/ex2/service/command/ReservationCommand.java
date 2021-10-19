package com.acme.ex2.service.command;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.acme.ex2.business.impl.ReservationCommandHandler;
import com.acme.ex2.model.Reservation;
import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.AbstractCommand.Usecase;

@Usecase(handlers = ReservationCommandHandler.class)
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
    	/*
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if(authentication != null) {
    		this.username = authentication.getName();
    	}
    	*/
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
