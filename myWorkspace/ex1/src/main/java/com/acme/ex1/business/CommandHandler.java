package com.acme.ex1.business;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.acme.ex1.service.AbstractCommand;

import javax.transaction.Transactional;

/*
classes annotated with @Handler will be transactional, lazy, etc.
 */
public interface CommandHandler {

	void handle(AbstractCommand command);

	@Transactional(value = Transactional.TxType.MANDATORY)
	@Component
	@Lazy
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface Handler {
		String value() default "";
	}
}
