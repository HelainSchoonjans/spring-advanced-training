package com.acme.ex1.repository;

import com.acme.ex1.model.Author;
import com.acme.ex1.model.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Integer> {

    Optional<Member> findByUsername(String username);

}
