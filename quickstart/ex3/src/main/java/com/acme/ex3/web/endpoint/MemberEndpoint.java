package com.acme.ex3.web.endpoint;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acme.ex3.service.CommandProcessor;
import com.acme.ex3.service.command.MemberRegistrationCommand;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
class MemberEndpoint {

	private final CommandProcessor processor;
	
	MemberEndpoint(CommandProcessor processor) {
		this.processor = processor;
	}

	@PostMapping("members")
	Mono<ResponseEntity<Void>> members(@RequestBody @Valid MemberRegistrationCommand command) {
		return processor.process(command)
			.map(cmd -> {
				URI uri = URI.create("/members/"+cmd.getMember().getId());
				return ResponseEntity.created(uri).build();
			});
	}
}
