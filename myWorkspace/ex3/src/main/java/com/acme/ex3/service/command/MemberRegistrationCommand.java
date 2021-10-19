package com.acme.ex3.service.command;

import javax.validation.constraints.NotBlank;

import com.acme.ex3.service.AbstractCommand;
import com.acme.ex3.service.AbstractCommand.Usecase;
import com.acme.ex3.business.impl.MemberRegistrationCommandHandler;
import com.acme.ex3.business.impl.NoopHandler;
import com.acme.ex3.model.Member;

@Usecase(handlers = {MemberRegistrationCommandHandler.class, NoopHandler.class},parallelHandling = false)
public class MemberRegistrationCommand extends AbstractCommand {

	private static final long serialVersionUID = 1L;

	// inputs
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    // output
    private Member member;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
