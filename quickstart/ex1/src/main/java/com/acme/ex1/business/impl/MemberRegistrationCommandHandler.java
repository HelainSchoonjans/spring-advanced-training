package com.acme.ex1.business.impl;

import java.util.Optional;

import org.springframework.context.event.EventListener;

import com.acme.ex1.business.CommandException;
import com.acme.ex1.business.CommandHandler;
import com.acme.ex1.business.CommandHandler.Handler;
import com.acme.ex1.model.Account;
import com.acme.ex1.model.Member;
import com.acme.ex1.service.AbstractCommand;
import com.acme.ex1.service.command.MemberRegistrationCommand;


@Handler
public class MemberRegistrationCommandHandler implements CommandHandler {

    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    @EventListener(MemberRegistrationCommand.class)
    public void handle(AbstractCommand command) {
        if(!(command instanceof MemberRegistrationCommand)){
            return;
        }

        MemberRegistrationCommand cmd = (MemberRegistrationCommand)command;

        /* TODO : 
        remplacer null ci dessous par un appel au repository pour obtenir le Member dont le username est cmd.getUsername()
        */
        Optional<Member> _member = null; 
        if(_member.isPresent()) {
            throw new CommandException("memberRegistration-account.already.exists", true);
        }

        Member member = new Member();
        member.setFirstname(cmd.getFirstname());
        member.setLastname(cmd.getLastname());

        Account account = new Account();
        // si nous stockons les mots de passe : 
        // account.setPassword(/*hachage de cmd.getPassword()*/);
        account.setUsername(cmd.getUsername());
        member.setAccount(account);

        if(cmd.getPreferences() != null) {
            // TODO: comprendre et décommenter les lignes ci dessous (préalable : ajouter une dépendance vers CategoryRepository)
            //List<Category> preferences = cmd.getPreferences().stream()
            //		.flatMap(i -> categoryRepository.findById(i).stream())
            //		.collect(Collectors.toList());
            //member.setPreferences(preferences);
        }
        // TODO : sauvegarde du member
        
        
        cmd.setMember(member);
        
        // si nous déléguons l'authentification : création du compte utilisateur sur ce dernier.
    }
}
