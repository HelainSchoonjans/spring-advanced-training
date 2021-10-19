package com.acme.ex1.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acme.ex1.business.CommandException;
import com.acme.ex1.service.AbstractCommand;
import com.acme.ex1.service.CommandProcessor;
import com.acme.ex1.service.ExceptionHandler;

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
	
	@Override
	public <T extends AbstractCommand> T process(T command){

		String cmdId = command.toString();
		logger.info("Received a command of type {} : {}", command.getClass().getSimpleName(), cmdId);

		try {
			logger.info("Is {} valid before handling ?", cmdId);
            command.validateStateBeforeHandling();
			logger.info("yes, {} is valid, return it", cmdId);
			
			// dispatch command to business layer (command handlers)
			ctx.publishEvent(command);
			
			logger.info("is {} valid after handling ?", cmdId);
			command.validateStateAfterHandling();
			logger.info("is {} valid after handling ?", cmdId);

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
