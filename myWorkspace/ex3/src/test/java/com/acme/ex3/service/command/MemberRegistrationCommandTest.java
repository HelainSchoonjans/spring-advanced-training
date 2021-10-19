package com.acme.ex3.service.command;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme.ex3.business.CommandException;
import com.acme.ex3.service.impl.CommandProcessorImpl;
import com.acme.ex3.service.command.MemberRegistrationCommand;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class MemberRegistrationCommandTest {

	@Autowired
    private CommandProcessorImpl processor;
    
    @Test
    void testMemberRegistrationCommandOnError() throws InterruptedException {
        MemberRegistrationCommand cmd = new MemberRegistrationCommand();
        cmd.setFirstname("Jane");
        cmd.setLastname("Doe");
        cmd.setUsername("jdoe");
        cmd.setPassword("azerty");    	
    	
        Mono<MemberRegistrationCommand> mono = this.processor.process(cmd);
        
        StepVerifier.create(mono)
        	.expectError(CommandException.class)
        	.verify();
    }
  
    @Test
    //@Sql(statements = "delete from Member where id>1") // ne fonctionne qu'avec JDBC
    void testMemberRegistrationCommand() throws InterruptedException {
    	
    	MemberRegistrationCommand cmd = new MemberRegistrationCommand();
        cmd.setFirstname("Jane");
        cmd.setLastname("Doe");
        cmd.setUsername("jane.doe"+Instant.now().toEpochMilli());
        cmd.setPassword("azerty");     

        Mono<MemberRegistrationCommand> mono = this.processor.process(cmd).log();
        
        StepVerifier.create(mono)
        	.expectNextMatches(command -> {
        		System.out.println(command.getMember());
        		return command.getMember() != null;
        	})
        	.verifyComplete();
    }
}
