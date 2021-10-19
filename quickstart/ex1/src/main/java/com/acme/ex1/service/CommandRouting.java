package com.acme.ex1.service;

import java.util.List;

import com.acme.ex1.business.CommandHandler;

public interface CommandRouting {

	List<CommandHandler> getHandlers(AbstractCommand cmd);

}
