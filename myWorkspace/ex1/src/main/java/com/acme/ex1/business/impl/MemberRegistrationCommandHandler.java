package com.acme.ex1.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.acme.ex1.business.CommandException;
import com.acme.ex1.business.CommandHandler;
import com.acme.ex1.business.CommandHandler.Handler;
import com.acme.ex1.model.Account;
import com.acme.ex1.model.Category;
import com.acme.ex1.model.Member;
import com.acme.ex1.repository.CategoryRepository;
import com.acme.ex1.repository.MemberRepository;
import com.acme.ex1.service.AbstractCommand;
import com.acme.ex1.service.command.MemberRegistrationCommand;

@Handler
public class MemberRegistrationCommandHandler implements CommandHandler {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository      memberRepository;
    private final CategoryRepository    categoryRepository;

    public MemberRegistrationCommandHandler(BCryptPasswordEncoder passwordEncoder, MemberRepository memberRepository,
            CategoryRepository categoryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @EventListener(MemberRegistrationCommand.class)
    public void handle(AbstractCommand command) {
        if (!(command instanceof MemberRegistrationCommand)) {
            return;
        }

        MemberRegistrationCommand cmd = (MemberRegistrationCommand) command;

        // here we just want to know if it exists; it would be better to add an exist method in the repository...
        // we are currently loading too much data
        Optional<Member> _member = memberRepository.findByAccountUsername(cmd.getUsername());
        if (_member.isPresent()) {
            throw new CommandException("memberRegistration-account.already.exists", true);
        }

        Member member = new Member();
        member.setFirstname(cmd.getFirstname());
        member.setLastname(cmd.getLastname());

        Account account = new Account();
        // si nous stockons les mots de passe : 
        account.setPassword(passwordEncoder.encode(cmd.getPassword()));
        account.setUsername(cmd.getUsername());
        member.setAccount(account);

        if (cmd.getPreferences() != null) {
            List<Category> preferences = cmd.getPreferences()
                    .stream()
                    .flatMap(i -> categoryRepository.findById(i)
                            .stream())
                    .collect(Collectors.toList());
            member.setPreferences(preferences);
        }
        memberRepository.save(member);

        cmd.setMember(member);

        // si nous déléguons l'authentification : création du compte utilisateur sur ce dernier.
    }
}
