package com.acme.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Category;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
