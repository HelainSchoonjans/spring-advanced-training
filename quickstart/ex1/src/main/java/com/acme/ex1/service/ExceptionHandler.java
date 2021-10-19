package com.acme.ex1.service;

public interface ExceptionHandler {

	void handleException(Exception ex, AbstractCommand command);
}
