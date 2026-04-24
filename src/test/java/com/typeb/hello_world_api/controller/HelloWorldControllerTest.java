package com.typeb.hello_world_api.controller;

import com.typeb.hello_world_api.exception.InvalidInputException;
import com.typeb.hello_world_api.service.HelloWorldService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloWorldController.class)
class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloWorldService helloWorldService;

    @Test
    @DisplayName("GET /hello-world?name=alice → 200 with correct body")
    void validName_returns200() throws Exception {
        when(helloWorldService.greet("alice")).thenReturn("Alice");

        mockMvc.perform(get("/hello-world").param("name", "alice"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Hello Alice"));
    }

    @Test
    @DisplayName("GET /hello-world?name=nancy → 400 with error body")
    void invalidName_returns400() throws Exception {
        when(helloWorldService.greet("nancy"))
                .thenThrow(new InvalidInputException("N–Z range"));

        mockMvc.perform(get("/hello-world").param("name", "nancy"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("GET /hello-world (no name param) → 400 with error body")
    void missingName_returns400() throws Exception {
        when(helloWorldService.greet(null))
                .thenThrow(new InvalidInputException("missing"));

        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("GET /hello-world?name= (empty) → 400 with error body")
    void emptyName_returns400() throws Exception {
        when(helloWorldService.greet(""))
                .thenThrow(new InvalidInputException("blank"));

        mockMvc.perform(get("/hello-world").param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("Response has no extra fields — only 'message' on success")
    void successBody_hasOnlyMessageField() throws Exception {
        when(helloWorldService.greet("alice")).thenReturn("Alice");

        mockMvc.perform(get("/hello-world").param("name", "alice"))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.message").value("Hello Alice"));
    }

    @Test
    @DisplayName("Response has no extra fields — only 'error' on failure")
    void errorBody_hasOnlyErrorField() throws Exception {
        when(helloWorldService.greet("zara"))
                .thenThrow(new InvalidInputException("N–Z"));

        mockMvc.perform(get("/hello-world").param("name", "zara"))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }
}