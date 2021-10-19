package com.acme.ex2.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.acme.ex2.business.CommandException;
import com.acme.ex2.business.CommandHandler;
import com.acme.ex2.business.CommandHandler.HandlingContext;
import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.AbstractCommand.Usecase;
import com.acme.ex2.service.CommandProcessor;
import com.acme.ex2.service.ExceptionHandler;

@Service
public class CommandProcessorImpl implements CommandProcessor {

	private Logger logger;

	private List<ExceptionHandler> exHandlers;

	private ApplicationContext ctx;

	public CommandProcessorImpl(Logger logger, List<ExceptionHandler> exHandlers, ApplicationContext ctx) {
		super();
		this.logger = logger;
		this.exHandlers = exHandlers;
		this.ctx = ctx;
	}
	
	@Transactional
	@Override
	public <T extends AbstractCommand> T process(T command){

		String cmdId = command.toString();
		logger.info("Received a command of type {} : {}", command.getClass().getSimpleName(), cmdId);

		HandlingContext handlingContext = new HandlingContext();
		
		try {
			logger.info("Is {} valid before handling ?", cmdId);
			command.validateStateBeforeHandling();
            logger.info("yes, {} is valid", cmdId);

			logger.info("Identify which handlers will handle {}", cmdId);
			Usecase usecase = command.getClass().getAnnotation(Usecase.class);

			List<CommandHandler> handlersForThisCommand = Stream.of(usecase.handlers())
					.map(handlerClass -> ctx.getBean(handlerClass))
					.collect(Collectors.toList());

			logger.info("{} handlers will deal with {}", handlersForThisCommand.size(), cmdId);
			if(!usecase.parallelHandling()){
				for (CommandHandler handler : handlersForThisCommand) {
					logger.info("ask {} to handle {}", handler.getClass().getName(), cmdId);
					// TODO : appel de la méthode handle sur handler
					handler.handle(command, handlingContext);

				}
			}
			else{
				@SuppressWarnings("rawtypes")
				CompletableFuture[] workers = handlersForThisCommand.stream()
						.map(h -> CompletableFuture.runAsync(() -> h.handle(command, handlingContext)))
						.toArray(handlersCount -> new CompletableFuture[handlersCount]);

				logger.info("waiting for the {} handlers to complete", workers.length);
				CompletableFuture.allOf(workers).join();
				logger.info("command handling completed");
			}


			logger.info("is {} valid after handling ?", cmdId);
			command.validateStateAfterHandling();
			logger.info("yes, {} is valid, return it", cmdId);
			
			handlingContext.getAfterCommit().forEach(runnable -> {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					public void afterCommit() {
						runnable.run();
					};
				});
			});
			
			return command;
		} catch (Exception e) {
			/*exemple de traitement d'exception : 
			 serialisation de la commande dans un fichier sous le nom cmd.toString()+".json"
			 et écriture de la stacktrace dans un fichier sous le nom cmd.toString()+".stacktrace.txt"
			 Voir DefaultExceptionHandler
			*/
			exHandlers.forEach(x -> x.handleException(e, command));
			
			if (e instanceof CommandException) {
				logger.warn("hum hum... "+e.getMessage());
				throw e;
			}
			else{
				e.printStackTrace();
				logger.error("hum hum... "+e.getMessage());
				throw new RuntimeException("Oops, I did it again...");
			}
		}
	}
}
