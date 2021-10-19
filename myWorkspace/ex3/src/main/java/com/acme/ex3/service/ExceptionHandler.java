package com.acme.ex3.service;

public interface ExceptionHandler {

	void handleException(Throwable ex, AbstractCommand command);
}
