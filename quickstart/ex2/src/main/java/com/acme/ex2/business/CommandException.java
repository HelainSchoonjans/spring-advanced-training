package com.acme.ex2.business;

public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 117400658952600303L;

	private boolean messageI18nKey;
	
	public CommandException(String message, boolean messageI18nKey) {
		super(message);
		this.messageI18nKey = messageI18nKey;
	}

	public CommandException(String message) {
		this(message, true);
	}
	
	public boolean isMessageI18nKey(){
		return this.messageI18nKey;
	}
}
