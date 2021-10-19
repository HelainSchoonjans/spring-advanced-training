package com.acme.ex1.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acme.ex1.business.CommandException;


@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(CommandException.class)
    String onCommandException(Model model, Exception e) {
        model.addAttribute("exception", e);
        return "_errors/command-exception";
    }

    //@ExceptionHandler(Throwable.class)
    String onOtherException() {
        return "_errors/other-exception";
    }
}
