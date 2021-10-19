package com.acme.ex1.service;

public interface CommandPreHandler {

	void beforeHandle(AbstractCommand command);
}
