package com.acme.ex3.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.ex3.business.CommandException;
import com.acme.ex3.business.CommandHandler;
import com.acme.ex3.business.CommandHandler.HandlingContext;
import com.acme.ex3.service.AbstractCommand;
import com.acme.ex3.service.AbstractCommand.Usecase;
import com.acme.ex3.service.CommandProcessor;
import com.acme.ex3.service.ExceptionHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

@Service
public class CommandProcessorImpl implements CommandProcessor {

	private final Logger logger;

	private final List<ExceptionHandler> exHandlers;

	private final ApplicationContext ctx;

	public CommandProcessorImpl(Logger logger, List<ExceptionHandler> exHandlers, ApplicationContext ctx) {
		super();
		this.logger = logger;
		this.exHandlers = exHandlers;
		this.ctx = ctx;
	}
	
	@Override
	@Transactional
	public <T extends AbstractCommand> Mono<T> process(T command){


		String cmdId = command.toString();
		logger.info("Received a command of type {} : {}", command.getClass().getSimpleName(), cmdId);

		if(!command.getClass().isAnnotationPresent(Usecase.class)) {
			return Mono.just(command);
		}
		
		HandlingContext handlingContext = new HandlingContext();

		logger.info("Identify which handlers will handle {}", cmdId);

		Usecase usecase = command.getClass().getAnnotation(Usecase.class);

		List<CommandHandler> handlersForThisCommand = Stream.of(usecase.handlers())
			.map(handlerClass -> ctx.getBean(handlerClass))
			.collect(Collectors.toList());

		logger.info("{} handlers will deal with {}", handlersForThisCommand.size(), cmdId);
		
		@SuppressWarnings("rawtypes")
		Mono[] monos = handlersForThisCommand
			.stream()
			.map(h -> {
				Mono<Void> mono = h.handle(command, handlingContext)
					.doOnSubscribe(subscription -> logger.info("ask {} to handle {}", h.getClass().getName(), cmdId))
					.doOnSuccess(nothing -> logger.info("{} successfully handled {}", h.getClass().getName(), cmdId));
				return mono;
			})
			.toArray(n -> new Mono[n]);
		// ou bien, sans le log :
		/*
		Mono[] monos =  handlersForThisCommand
				.stream()
				.map(h -> h.handle(command, handlingContext))
				.toArray(n -> new Mono[n]);
		*/

		@SuppressWarnings("unchecked")
		Mono<Void> handling = usecase.parallelHandling() ? ParallelFlux.from(monos).then() : Flux.concat(monos).then();
		
		return Mono
			.fromRunnable(() -> {
				logger.info("Is {} valid before handling ?", cmdId);
				command.validateStateBeforeHandling();	
			})
			.then(handling)
			.then(Mono.fromRunnable(() -> {
				logger.info("is {} valid after handling ?", cmdId);
				command.validateStateAfterHandling();	
				logger.info("yes, {} is valid, return it", cmdId);
			}))
			.onErrorMap(error -> {
				exHandlers.forEach(x -> x.handleException(error, command));
				
				if (error instanceof CommandException) {
					logger.warn("hum hum... "+error.getMessage());
					return error;
				}
				else{
					logger.error("hum hum... "+error.getMessage());
					return new RuntimeException("Oops, I did it again...");
				}
			})
			.thenReturn(command);
	}
}
