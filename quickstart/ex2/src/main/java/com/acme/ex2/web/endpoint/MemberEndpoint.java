package com.acme.ex2.web.endpoint;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acme.ex2.service.CommandProcessor;
import com.acme.ex2.service.command.MemberRegistrationCommand;

@RestController
public class MemberEndpoint {

    private final CommandProcessor processor;

    public MemberEndpoint(CommandProcessor processor) {
        this.processor = processor;
    }

    @PostMapping("members")
    ResponseEntity<Void> members(@RequestBody @Valid MemberRegistrationCommand command) {
        MemberRegistrationCommand result = processor.process(command);
        String uri = "/members/"+result.getMember().getId();
        return ResponseEntity.created(URI.create(uri)).build();
    }
}
