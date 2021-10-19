package com.acme.ex3.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.acme.ex3.model.Member;

import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<Member, Integer> {

	Mono<Member> findByUsername(String username);
	
	Mono<Boolean> existsByUsername(String username);
}
