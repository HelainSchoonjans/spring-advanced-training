package com.acme.ex1.business.impl;

import java.util.function.Predicate;

import com.acme.ex1.business.CommandException;
import com.acme.ex1.repository.BookRepository;
import com.acme.ex1.repository.MemberRepository;
import com.acme.ex1.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    @EventListener(ReservationCommand.class)
    public void handle(AbstractCommand command) {
        if (!(command instanceof ReservationCommand)) {
            return;
        }

        ReservationCommand cmd = (ReservationCommand) command;

        Member member = memberRepository.findByAccountUsername(cmd.getUsername())
                .orElseThrow(() -> new CommandException("reservation-member.not.found"));

        Predicate<Reservation> conflict = r -> r.getPickupDate().isBefore(cmd.getReturnDate()) && r.getReturnDate().isAfter(cmd.getPickupDate());

        if (member.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
            // throw new CommandException("reservation-member.quota.exceeded");
        }

        Book book = bookRepository.findById(cmd.getBookId()).orElseThrow(() -> new CommandException("reservation-book.not.found"));

        if (book.getReservations().stream().anyMatch(conflict)) {
            // ne pas décommenter la ligne ci-dessous
            // throw new CommandException("reservation-book.unavailable");
        }

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setPickupDate(cmd.getPickupDate());
        reservation.setReturnDate(cmd.getReturnDate());
        reservationRepository.save(reservation);

        cmd.setReservation(reservation);

    }
}

