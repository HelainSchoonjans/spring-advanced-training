package com.acme.ex2.business;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acme.ex2.service.AbstractCommand;

public interface CommandHandler {

	class HandlingContext{
		private final List<Runnable> afterCommit = new ArrayList<>();

		public void doAfterCommit(Runnable runnable){
			this.afterCommit.add(runnable);
		}

		public Iterable<Runnable> getAfterCommit(){
			return this.afterCommit;
		}
	}

	void handle(AbstractCommand command, HandlingContext handlingContext);
	
	@Component
	@Lazy
	@Transactional(propagation = Propagation.MANDATORY)
	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
	@interface Handler {
		String value() default "";
	}
}
