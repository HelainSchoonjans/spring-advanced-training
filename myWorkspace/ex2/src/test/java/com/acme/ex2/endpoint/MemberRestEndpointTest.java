package com.acme.ex2.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class MemberRestEndpointTest {

	@Autowired
	private MockMvc mockMvc;

	
    @Test
    void testMembers200() throws Exception{
        mockMvc.perform(get("/members")
                .accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andDo(print());
    }

    @Test
    void testMember200() throws Exception{
        mockMvc.perform(get("/members/1")
                .accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andDo(print());
    }

    @Test
    void testMember404() throws Exception{
        mockMvc.perform(get("/members/999")
                .accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isNotFound());
    }
    
    private ObjectMapper jackson = new ObjectMapper();
    
    @Test
    @Sql(statements = {"delete from Member_category",  "delete from Member where id>1"})
    void testMembers201() throws Exception {
    	
    	JsonNode newMember = jackson.createObjectNode()
    			.put("firstname", "Jane")
    			.put("lastname", "Doe")
                .put("username", "jane.doe")
                .put("password", "azerty");
    	
        mockMvc.perform(post("/members")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(newMember.toString()))
        	.andExpect(status().isCreated());
    }
    
    @Test
    void testMembersWithMissingUsername() throws Exception {
    	
        JsonNode newMember = jackson.createObjectNode()
                .put("firstname", "Jane")
                .put("lastname", "")
                .put("username", "jdoe")
                .put("password", "azerty");
        
        mockMvc.perform(post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newMember.toString()))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void testMembersWithConflictUsername() throws Exception {
        JsonNode newMember = jackson.createObjectNode()
                .put("firstname", "Jane")
                .put("lastname", "Doe")
                .put("username", "jdoe")
                .put("password", "azerty");
        
        mockMvc.perform(post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newMember.toString()))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}

