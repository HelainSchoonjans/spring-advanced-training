package com.acme.ex2.service.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.acme.ex2.business.CommandException;
import com.acme.ex2.service.command.MemberRegistrationCommand;
import com.acme.ex2.service.impl.CommandProcessorImpl;

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
        cmd.setPreferences(List.of(1, 2));
        assertThrows(CommandException.class, () -> this.processor.process(cmd));
    }

    @Test
    @Sql(statements = {"delete from Member_category", "delete from Member where id>1"})
    void testMemberRegistrationCommand() {
        MemberRegistrationCommand cmd = new MemberRegistrationCommand();
        cmd.setFirstname("Jane");
        cmd.setLastname("Doe");
        cmd.setUsername("jane.doe");
        cmd.setPassword("azerty");
        cmd.setPreferences(List.of(2, 3));
        cmd = this.processor.process(cmd);
        assertNotNull(cmd.getMember());
    }

}
