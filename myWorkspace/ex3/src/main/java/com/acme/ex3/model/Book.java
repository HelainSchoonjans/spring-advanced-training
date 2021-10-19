package com.acme.ex3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Book {

	@Id
	private Integer id;

	private String title;
    
	public Book() {
		super();
	}
	
	public Book(String title) {
		this();
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
        	return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
			return this.getId() != null && this.getId().equals(((Book)obj).getId());
		}
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", id=" + getId() + "]";
	}   
}
