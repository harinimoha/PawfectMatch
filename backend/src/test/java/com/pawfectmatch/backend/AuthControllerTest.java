package com.pawfectmatch.backend;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import com.pawfectmatch.backend.users.User;
import com.pawfectmatch.backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Creating a parent user to satisfy the FK constraint
        User user = new User();
        user.setEmail("login@test.com");
        user.setPassword("correctPassword");
        user.setRole("ADOPTER");
        userRepository.save(user);
    }

    @Test
    void loginSuccess() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "login@test.com");
        loginJson.put("password", "correctPassword");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .content(loginJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        User loggedInUser = objectMapper.readValue(response.getContentAsString(), User.class);
        assertEquals("login@test.com", loggedInUser.getEmail());
    }

    @Test
    void loginFailureWrongPassword() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "login@test.com");
        loginJson.put("password", "wrongPassword");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .content(loginJson.toString()))
                .andReturn().getResponse();

        // Return 401 Unauthorized
        assertEquals(401, response.getStatus());
        assertEquals("Invalid email or password", response.getContentAsString());
    }

    @Test
    void loginFailureUserNotFound() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "nonexistent@test.com");
        loginJson.put("password", "somePassword");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .content(loginJson.toString()))
                .andReturn().getResponse();

        assertEquals(401, response.getStatus());
    }
}