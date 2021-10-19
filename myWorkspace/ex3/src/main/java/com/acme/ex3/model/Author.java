package com.acme.ex3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Author {

	@Id
	private Integer id;

	private String firstname;
	
	private String lastname;
    
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


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


	@Override
	public int hashCode() {
		return this.id == null ? super.hashCode() : (this.id).hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		else if(obj == this) {
			return true;
		}
		else if(obj.getClass() != this.getClass()) {
			return false;
		}
		else {
			return this.getId() != null && this.getId().equals(((Author)obj).getId());
		}
	}
}
