package com.acme.ex2.service;

import java.util.List;

import com.acme.ex2.business.CommandHandler;

public interface CommandRouting {

	List<CommandHandler> getHandlers(AbstractCommand cmd);

}
