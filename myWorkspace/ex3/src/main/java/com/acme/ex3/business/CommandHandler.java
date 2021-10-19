package com.acme.ex3.business;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.acme.ex3.service.AbstractCommand;

import reactor.core.publisher.Mono;

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

	Mono<Void> handle(AbstractCommand command, HandlingContext handlingContext);
	
	@Component
	@Lazy
	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
	@interface Handler {
		String value() default "";
	}
}
