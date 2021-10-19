package com.acme.ex3.service.command;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import com.acme.ex3.business.impl.ReservationCommandHandler;
import com.acme.ex3.model.Reservation;
import com.acme.ex3.service.AbstractCommand;
import com.acme.ex3.service.AbstractCommand.Usecase;

@Usecase(handlers = ReservationCommandHandler.class)
public class ReservationCommand extends AbstractCommand {

	private static final long serialVersionUID = 1L;

	// inputs
    private int bookId;
    //@NotBlank 
    // https://github.com/spring-projects/spring-security/issues/5207#issuecomment-607391018
    private String username;
    @NotNull @FutureOrPresent
    private LocalDate pickupDate;
    @NotNull @Future
    private LocalDate returnDate;
    // output
    private Reservation reservation;

    public ReservationCommand() {
    	// https://github.com/spring-projects/spring-security/issues/5207#issuecomment-607391018
        /*
        SecurityContext securityContext = ReactiveSecurityContextHolder.getContext().block();

        if(securityContext != null && securityContext.getAuthentication() != null) {
    		this.username = securityContext.getAuthentication().getName();
    		System.out.println(this.username);
    	}*/
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

