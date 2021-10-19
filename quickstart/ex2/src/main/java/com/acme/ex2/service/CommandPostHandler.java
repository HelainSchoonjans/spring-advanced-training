package com.acme.ex2.service;

public interface CommandPostHandler {

	void afterHandle(AbstractCommand command);
}
