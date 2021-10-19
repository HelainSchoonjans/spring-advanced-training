package com.acme.ex2.business.impl;

import java.util.function.Predicate;

import com.acme.ex2.business.CommandException;
import com.acme.ex2.business.CommandHandler;
import com.acme.ex2.business.CommandHandler.Handler;
import com.acme.ex2.model.Book;
import com.acme.ex2.model.Member;
import com.acme.ex2.model.Reservation;
import com.acme.ex2.repository.BookRepository;
import com.acme.ex2.repository.MemberRepository;
import com.acme.ex2.repository.ReservationRepository;
import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.command.ReservationCommand;

@Handler
public class ReservationCommandHandler implements CommandHandler {

	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	
    public ReservationCommandHandler(BookRepository bookRepository, MemberRepository memberRepository,
			ReservationRepository reservationRepository) {
		super();
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
		this.reservationRepository = reservationRepository;
	}

	@Override
    public void handle(AbstractCommand command, HandlingContext handlingContext) {
        if (!(command instanceof ReservationCommand)) {
            return;
        }

        ReservationCommand cmd = (ReservationCommand) command;

        Member member = this.memberRepository.findByAccountUsername(cmd.getUsername())
        		.orElseThrow(() -> new CommandException("reservation-member.not.found"));

        Predicate<Reservation> conflict = r -> r.getPickupDate().isBefore(cmd.getReturnDate()) && r.getReturnDate().isAfter(cmd.getPickupDate());

        if (member.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
            // throw new CommandException("reservation-member.quota.exceeded");
        }

        Book book = this.bookRepository.findById(cmd.getBookId())
        		.orElseThrow(() -> new CommandException("reservation-book.not.found"));

        if (book.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
        	// throw new CommandException("reservation-book.unavailable");
        }

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setPickupDate(cmd.getPickupDate());
        reservation.setReturnDate(cmd.getReturnDate());

        reservation = this.reservationRepository.save(reservation);

        cmd.setReservation(reservation);

    }
}

