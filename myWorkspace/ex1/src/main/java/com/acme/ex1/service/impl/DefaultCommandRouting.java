package com.acme.ex1.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acme.ex1.business.CommandHandler;
import com.acme.ex1.service.AbstractCommand;
import com.acme.ex1.service.AbstractCommand.Usecase;
import com.acme.ex1.service.CommandRouting;

public class DefaultCommandRouting implements CommandRouting {

	private final Map<Class<? extends AbstractCommand>, List<CommandHandler>> routing = new ConcurrentHashMap<>();

	@Autowired
	private ApplicationContext ctx;

	@Override
	public List<CommandHandler> getHandlers(AbstractCommand cmd) {

		List<CommandHandler> handlersForThisCommand = this.routing.computeIfAbsent(cmd.getClass(), c -> {;
			Usecase usecase = cmd.getClass().getAnnotation(Usecase.class);
			return Stream.of(usecase.handlers()).map(x -> ctx.getBean(x)).collect(Collectors.toList());
		});
		return handlersForThisCommand;
	}
}
