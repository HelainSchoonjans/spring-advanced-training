package com.acme.ex3.business.impl;

import org.springframework.stereotype.Component;

import com.acme.ex3.business.CommandHandler;
import com.acme.ex3.service.AbstractCommand;

import reactor.core.publisher.Mono;

@Component
public class NoopHandler implements CommandHandler {

	@Override
	public Mono<Void> handle(AbstractCommand command, HandlingContext handlingContext) {
		
		//return Mono.delay(Duration.ofSeconds(4)).then();
		return Mono.empty();
	}
}
