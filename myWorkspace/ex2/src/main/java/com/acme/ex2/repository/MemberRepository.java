package com.acme.ex2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Member;


public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByAccountUsername(String username);
}
