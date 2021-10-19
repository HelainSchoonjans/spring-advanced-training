package com.acme.ex2.web.endpoint;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.acme.ex2.business.CommandException;

@RestControllerAdvice
class _ControllerAdvice {

	private final ResourceBundleMessageSource messages;

	public _ControllerAdvice(ResourceBundleMessageSource messages) {
		super();
		this.messages = messages;
	}

    class Error {
        public String cause;
        public Object data;

        Error(BindingResult br) {
            Map<String, String> map = br.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            this.cause = "validation";
            this.data = map;
        }     

		Error(String cause, Object data) {
			this.cause = cause;
			this.data = data;
		}

		Error(Object data) {
			this(null,data);
		}
    }

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Error validationHandler(BindException ex){
	    return new Error(ex.getBindingResult());
	}
	
	@ExceptionHandler(CommandException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Error onCommandException(CommandException e, Locale locale){
		String message = messages.getMessage(e.getMessage(), null, locale);
		return new Error(message);
	}	
}
