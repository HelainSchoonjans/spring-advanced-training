package com.acme.ex3.web.endpoint;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acme.ex3.service.CommandProcessor;
import com.acme.ex3.service.command.ReservationCommand;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
class ReservationEndpoint {

	private final CommandProcessor processor;
	
	ReservationEndpoint(CommandProcessor processor) {
		this.processor = processor;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("reservations")
	Mono<ResponseEntity<Void>> reservations(@RequestBody @Valid ReservationCommand command, Authentication auth) {
		if(!StringUtils.hasText(command.getUsername())) {
			command.setUsername(auth.getName());
		}
		return processor.process(command)
			.map(cmd -> {
				URI uri = URI.create("/reservations/"+cmd.getReservation().getId());
				return ResponseEntity.created(uri).build();
			});
	}
}

