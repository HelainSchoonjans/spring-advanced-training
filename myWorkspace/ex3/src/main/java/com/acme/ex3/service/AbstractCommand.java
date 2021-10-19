package com.acme.ex3.service;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.acme.ex3.business.CommandException;
import com.acme.ex3.business.CommandHandler;

public abstract class AbstractCommand  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public void validateStateBeforeHandling() {
		
		Set<ConstraintViolation<AbstractCommand>> constraintViolations = validator.validate(this);
		if(!constraintViolations.isEmpty()){
			throw new CommandException(constraintViolations.toString(), false);
		}
		
	}

	public void validateStateAfterHandling() {}


	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Usecase{
		Class<? extends CommandHandler>[] handlers();

		boolean parallelHandling() default false;

		String requiredAuthority() default "";
		
		boolean secured() default false;
	}
}
