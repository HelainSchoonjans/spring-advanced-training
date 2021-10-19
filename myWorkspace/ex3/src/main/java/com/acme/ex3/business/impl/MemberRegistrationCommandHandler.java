package com.acme.ex3.business.impl;

import org.springframework.stereotype.Component;

import com.acme.ex3.business.CommandException;
import com.acme.ex3.business.CommandHandler;
import com.acme.ex3.service.AbstractCommand;
import com.acme.ex3.model.Member;
import com.acme.ex3.repository.MemberRepository;
import com.acme.ex3.service.command.MemberRegistrationCommand;

import reactor.core.publisher.Mono;

@Component
public class MemberRegistrationCommandHandler implements CommandHandler {

	private final MemberRepository memberRepository;

	public MemberRegistrationCommandHandler(MemberRepository memberRepository) {
		super();
		this.memberRepository = memberRepository;
	}

	@Override
	public Mono<Void> handle(AbstractCommand command, HandlingContext handlingContext) {
		
		if (!(command instanceof MemberRegistrationCommand)) {
			return Mono.empty();
		}

		MemberRegistrationCommand cmd = (MemberRegistrationCommand) command;

		return this.memberRepository.existsByUsername(cmd.getUsername())
			.filter(exists -> exists == false /*ou !exists*/)
			.map(exists -> {
				Member m = new Member();
				m.setFirstname(cmd.getFirstname());
				m.setLastname(cmd.getLastname());
				m.setUsername(cmd.getUsername());
				return m;
			})
			.flatMap(member -> this.memberRepository.save(member))
			.map(savedMember -> {
				cmd.setMember(savedMember);
				return cmd;
			})
			.switchIfEmpty(Mono.error(new CommandException("memberRegistration-account.already.exists")))
			.then(); // .then() pour ne pas retourner un Mono<MemberRegistrationCommand>
	}
}
