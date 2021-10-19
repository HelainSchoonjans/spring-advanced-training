package com.acme.ex2.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testBooks200() throws Exception {
        mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void testSearch200() throws Exception {
        mockMvc.perform(get("/books/search/byTitleAndAuthor")
                .param("title", "a")
                .param("author", "g")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
    
    @Test
    void testBook200() throws Exception {
        mockMvc.perform(get("/books/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void testBook404() throws Exception {
        mockMvc.perform(get("/books/999")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteBook405() throws Exception {
         mockMvc.perform(delete("/books/1"))
             .andExpect(status().isMethodNotAllowed());
    }
    
}