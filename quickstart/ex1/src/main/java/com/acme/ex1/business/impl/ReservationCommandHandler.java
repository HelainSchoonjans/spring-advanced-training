package com.acme.ex1.business.impl;

import java.util.function.Predicate;

import org.springframework.context.event.EventListener;

import com.acme.ex1.business.CommandHandler;
import com.acme.ex1.business.CommandHandler.Handler;
import com.acme.ex1.model.Book;
import com.acme.ex1.model.Member;
import com.acme.ex1.model.Reservation;
import com.acme.ex1.service.AbstractCommand;
import com.acme.ex1.service.command.MemberRegistrationCommand;
import com.acme.ex1.service.command.ReservationCommand;

@Handler
public class ReservationCommandHandler implements CommandHandler {

    @Override
    @EventListener(ReservationCommand.class)    
    public void handle(AbstractCommand command) {
        if (!(command instanceof ReservationCommand)) {
            return;
        }

        ReservationCommand cmd = (ReservationCommand) command;

        /* TODO : 
		Remplacer null ci dessous par un appel au repository pour obtenir le Member dont le username est cmd.getUsername()
        S'il n'y en pas : new CommandException("reservation-member.not.found")
        Suggestion : utiliser la méthode orElseThrow de la classe Optional
        */
        Member member = null;

        Predicate<Reservation> conflict = r -> r.getPickupDate().isBefore(cmd.getReturnDate()) && r.getReturnDate().isAfter(cmd.getPickupDate());

        if (member.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
            // throw new CommandException("reservation-member.quota.exceeded");
        }

        /* TODO : 
		Remplacer null ci dessous par un appel au repository pour obtenir le livre dont l'id est cmd.getBookId()
        S'il n'y en pas : new CommandException("reservation-book.not.found");
        Suggestion : utiliser la méthode orElseThrow de la classe Optional
        */
        Book book = null;

        if (book.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
        	// throw new CommandException("reservation-book.unavailable");
        }

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setPickupDate(cmd.getPickupDate());
        reservation.setReturnDate(cmd.getReturnDate());
        // TODO : sauvegarde de la réservation

        cmd.setReservation(reservation);

    }
}

