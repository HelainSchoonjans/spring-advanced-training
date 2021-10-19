package com.acme.ex2.service.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.acme.ex2.service.impl.CommandProcessorImpl;

@SpringBootTest
public class ReservationCommandTest {

    @Autowired
    private CommandProcessorImpl processor;

    @Test
    @Sql(statements = "delete from Reservation")
    void testReservationCommand() {
        ReservationCommand cmd = new ReservationCommand();
        cmd.setBookId(1);
        cmd.setUsername("jdoe");
        cmd.setPickupDate(LocalDate.now().plusDays(1));
        cmd.setReturnDate(LocalDate.now().plusDays(10));
        cmd = this.processor.process(cmd);
        assertNotNull(cmd.getReservation());
        System.out.println(cmd.getReservation());
    }
}
