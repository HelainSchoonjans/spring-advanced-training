package com.acme.ex2.service.impl;

import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.CommandPostHandler;
import com.acme.ex2.service.CommandPreHandler;


public class SerializerPrePostHandlerImpl implements CommandPostHandler, CommandPreHandler {

	@Override
	public void beforeHandle(AbstractCommand command) {
		String filename = command.toString() + ".in.json";
		log(command, filename);
	}

	@Override
	public void afterHandle(AbstractCommand command) {
		String filename = command.toString() + ".out.json";
		log(command, filename);
	}

	private void log(AbstractCommand command, String filename) {
		System.out.println("serialize " + command.toString()+" to "+filename);
	}
}
