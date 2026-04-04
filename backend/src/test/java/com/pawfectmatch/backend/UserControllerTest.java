package com.pawfectmatch.backend;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import com.pawfectmatch.backend.users.User;
import com.pawfectmatch.backend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    // Helper method: create a valid user
    private User createTestUser(String email, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");
        user.setRole(role);
        return user;
    }

    @Test
    void getAllUsers() throws Exception {
        userRepository.save(createTestUser("test1@example.com", "ADOPTER"));

        MockHttpServletResponse response = mockMvc.perform(get("/users"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<User> users = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<User>>() {}
        );

        assertTrue(users.size() > 0);
    }

    @Test
    void getUserById() throws Exception {
        User saved = userRepository.save(createTestUser("test2@example.com", "PROVIDER"));

        MockHttpServletResponse response = mockMvc.perform(get("/users/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(saved.getId(), receivedJson.get("id").asInt());
        assertEquals("test2@example.com", receivedJson.get("email").asText());
    }

    @Test
    void createUser() throws Exception {
        ObjectNode userJson = objectMapper.createObjectNode();
        userJson.put("email", "newuser@example.com");
        userJson.put("password", "secret123");
        userJson.put("role", "ADOPTER");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/users")
                                .contentType("application/json")
                                .content(userJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        User returnedUser = objectMapper.readValue(response.getContentAsString(), User.class);
        assertNotNull(returnedUser.getId());

        User savedInDb = userRepository.findById(returnedUser.getId()).get();
        assertEquals("newuser@example.com", savedInDb.getEmail());
    }

    @Test
    void updateUser() throws Exception {
        User saved = userRepository.save(createTestUser("old@example.com", "ADOPTER"));

        ObjectNode updatedJson = objectMapper.createObjectNode();
        updatedJson.put("email", "new@example.com");
        updatedJson.put("password", "newpassword");
        updatedJson.put("role", "PROVIDER");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/users/" + saved.getId())
                                .contentType("application/json")
                                .content(updatedJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        User updatedUser = userRepository.findById(saved.getId()).get();
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("PROVIDER", updatedUser.getRole());
    }

    @Test
    void deleteUser() throws Exception {
        User saved = userRepository.save(createTestUser("delete@example.com", "ADOPTER"));

        assertTrue(userRepository.findById(saved.getId()).isPresent());

        MockHttpServletResponse response = mockMvc.perform(delete("/users/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertFalse(userRepository.findById(saved.getId()).isPresent());
    }
}