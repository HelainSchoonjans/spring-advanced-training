package com.acme.ex1.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
public class Account {

    @NotEmpty
    private String username;

    private String password;

    public Account(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
    
    public Account() {
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
}
