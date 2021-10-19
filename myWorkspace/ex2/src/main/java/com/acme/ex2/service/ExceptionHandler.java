package com.acme.ex2.service;

public interface ExceptionHandler {

	void handleException(Exception ex, AbstractCommand command);
}
