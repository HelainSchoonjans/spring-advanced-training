package com.acme.ex3.business.impl;

import org.springframework.stereotype.Component;

import com.acme.ex3.business.CommandException;
import com.acme.ex3.business.CommandHandler;
import com.acme.ex3.service.AbstractCommand;
import com.acme.ex3.model.Reservation;
import com.acme.ex3.repository.BookRepository;
import com.acme.ex3.repository.MemberRepository;
import com.acme.ex3.repository.ReservationRepository;
import com.acme.ex3.service.command.ReservationCommand;

import reactor.core.publisher.Mono;

@Component
public class ReservationCommandHandler implements CommandHandler {

	private final ReservationRepository reservationRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	
	
	public ReservationCommandHandler(ReservationRepository reservationRepository, BookRepository bookRepository,
			MemberRepository memberRepository) {
		super();
		this.reservationRepository = reservationRepository;
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
	}


	@Override
	public Mono<Void> handle(AbstractCommand command, HandlingContext handlingContext) {
        if(!(command instanceof ReservationCommand)){
            return Mono.empty();
        }

        ReservationCommand cmd = (ReservationCommand)command;
       
		return this.bookRepository.findById(cmd.getBookId())
			.switchIfEmpty(Mono.error(new CommandException("reservation-book.not.found")))
			.map(book -> {
				Reservation reservation = new Reservation();
				reservation.setBookId(book.getId());
				return reservation;
			})
			.flatMap(reservation -> {
				return this.memberRepository.findByUsername(cmd.getUsername())
					.switchIfEmpty(Mono.error(new CommandException("reservation-member.not.found")))
					.map(member -> {
						reservation.setMemberId(member.getId());
						return reservation;
					});
			})
			.flatMap(reservation -> {
				reservation.setPickupdate(cmd.getPickupDate());
				reservation.setReturndate(cmd.getReturnDate());
				return this.reservationRepository.save(reservation);
			})
			.map(savedReservation -> {
				cmd.setReservation(savedReservation);
				return cmd;
			})
			.then(); // .then() pour ne pas retourner un Mono<ReservationCommand>
	}
}
