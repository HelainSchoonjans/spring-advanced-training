package com.acme.ex1.model;

import javax.persistence.Entity;

@Entity // suppose une table Category possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
public class Category extends AbstractPersistentEntity<Integer> {

	// et une colonne name
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
