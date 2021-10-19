package com.acme.ex2.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.acme.ex2.business.CommandException;
import com.acme.ex2.business.CommandHandler;
import com.acme.ex2.business.CommandHandler.Handler;
import com.acme.ex2.model.Account;
import com.acme.ex2.model.Category;
import com.acme.ex2.model.Member;
import com.acme.ex2.repository.CategoryRepository;
import com.acme.ex2.repository.MemberRepository;
import com.acme.ex2.service.AbstractCommand;
import com.acme.ex2.service.command.MemberRegistrationCommand;


@Handler
public class MemberRegistrationCommandHandler implements CommandHandler {

    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;
	
    public MemberRegistrationCommandHandler(MemberRepository memberRepository, CategoryRepository categoryRepository) {
		super();
		this.memberRepository = memberRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
    public void handle(AbstractCommand command, HandlingContext handlingContext) {
        if(!(command instanceof MemberRegistrationCommand)){
            return;
        }

        MemberRegistrationCommand cmd = (MemberRegistrationCommand)command;

        Optional<Member> _member = this.memberRepository.findByAccountUsername(cmd.getUsername()); 
        if(_member.isPresent()) {
            throw new CommandException("memberRegistration-account.already.exists", true);
        }

        Member member = new Member();
        member.setFirstname(cmd.getFirstname());
        member.setLastname(cmd.getLastname());

        Account account = new Account();
        // si nous stockons les mots de passe : 
        //account.setPassword(passwordEncoder.encode(cmd.getPassword()));
        account.setUsername(cmd.getUsername());
        member.setAccount(account);

        if(cmd.getPreferences() != null) {
            List<Category> preferences = cmd.getPreferences().stream()
            		.flatMap(i -> categoryRepository.findById(i).stream())
            		.collect(Collectors.toList());
            member.setPreferences(preferences);
        }
        // TODO : sauvegarde du member
        member = this.memberRepository.save(member);
        
        cmd.setMember(member);
        
        // si nous déléguons l'authentification : création du compte utilisateur sur ce dernier.
    }
}
