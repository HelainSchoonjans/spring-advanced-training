package com.acme.ex1.service;

public interface CommandProcessor {

	
	<T extends AbstractCommand> T process(T command);

}
