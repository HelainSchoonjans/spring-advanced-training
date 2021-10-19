package com.acme.ex2.web.endpoint;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acme.ex2.service.CommandProcessor;
import com.acme.ex2.service.command.ReservationCommand;

@RestController
@CrossOrigin
public class ReservationEndpoint {

    private final CommandProcessor processor;

    public ReservationEndpoint(CommandProcessor processor) {
        this.processor = processor;
    }

    @PostMapping("reservations")
    ResponseEntity<Void> reservations(@RequestBody @Valid ReservationCommand command){
        ReservationCommand result = processor.process(command);
        String uri = "/reservations/"+result.getReservation().getId();
        return ResponseEntity.created(URI.create(uri)).build();
    }
}
