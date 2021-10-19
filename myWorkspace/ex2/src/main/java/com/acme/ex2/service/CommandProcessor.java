package com.acme.ex2.service;

public interface CommandProcessor {

	
	<T extends AbstractCommand> T process(T command);

}
