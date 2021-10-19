package com.acme.ex3.service.command;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme.ex3.service.impl.CommandProcessorImpl;
import com.acme.ex3.service.command.ReservationCommand;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ReservationCommandTest {

	@Autowired
    private CommandProcessorImpl processor;


    @Test
    void testReservationCommand() {
        ReservationCommand cmd = new ReservationCommand();
        cmd.setBookId(1);
        cmd.setUsername("jdoe");
        cmd.setPickupDate(LocalDate.now().plusDays(1));
        cmd.setReturnDate(LocalDate.now().plusDays(10));
        
        Mono<ReservationCommand> mono = this.processor.process(cmd).log();
        
        StepVerifier.create(mono)
        	.expectNextMatches(command -> {
        		System.out.println(command);
        		return command.getReservation() != null;
        	})
        	.verifyComplete();
    }
}
