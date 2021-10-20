package com.acme.ex1.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

@Entity // suppose une table Category possédant une colonne id (cf. propriété héritée de AbstractPersistentEntity)
// Can be stored in level 2 cache to save some queries
@Cacheable
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
