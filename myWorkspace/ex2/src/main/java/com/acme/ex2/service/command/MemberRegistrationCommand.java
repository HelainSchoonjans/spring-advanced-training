package com.acme.ex2.service.command;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.acme.ex2.business.impl.MemberRegistrationCommandHandler;
import com.acme.ex2.model.Member;
import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.AbstractCommand.Usecase;

@Usecase(handlers = MemberRegistrationCommandHandler.class)
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
    private List<Integer> preferences;

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

    public List<Integer> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Integer> preferences) {
        this.preferences = preferences;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
