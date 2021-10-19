package com.acme.ex3.service;

import reactor.core.publisher.Mono;

public interface CommandProcessor {

	
	<T extends AbstractCommand> Mono<T> process(T command);

}
