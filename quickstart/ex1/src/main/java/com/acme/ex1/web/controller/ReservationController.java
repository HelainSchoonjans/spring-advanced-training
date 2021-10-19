package com.acme.ex1.web.controller;


import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.acme.ex1.service.CommandProcessor;
import com.acme.ex1.service.command.ReservationCommand;


@Controller
public class ReservationController {

	private final CommandProcessor processor;
	
    public ReservationController(CommandProcessor processor) {
		super();
		this.processor = processor;
	}

	@PostMapping("/reservations")
    public String borrow(@Valid @ModelAttribute("command") ReservationCommand command, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("command", command);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX+"command", br);
            return "redirect:/books/" + command.getBookId();
        }
        
        processor.process(command);

        return "redirect:/books";
    }
    
}
