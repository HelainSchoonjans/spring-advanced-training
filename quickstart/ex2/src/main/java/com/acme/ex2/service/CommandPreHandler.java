package com.acme.ex2.service;

public interface CommandPreHandler {

	void beforeHandle(AbstractCommand command);
}
