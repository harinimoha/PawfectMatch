package com.pawfectmatch.backend;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import com.pawfectmatch.backend.providers.Provider;
import com.pawfectmatch.backend.providers.ProviderRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Creating a parent user to satisfy the FK constraint
        User user = new User();
        user.setEmail("provider" + System.currentTimeMillis() + "@shelter.com");
        user.setPassword("securePass123");
        user.setRole("PROVIDER");
        testUser = userRepository.save(user);
    }

    // Helper method: creating a valid adopter with all mandatory fields
    private Provider createTestProvider(String orgName, String phone) {
        Provider provider = new Provider();
        provider.setUserId(testUser.getId());
        provider.setOrgName(orgName);
        provider.setPhone(phone);
        provider.setAddress("123 Rescue Way");
        provider.setFoundedYear(2010);
        return provider;
    }

    @Test
    void getAllProviders() throws Exception {
        providerRepository.save(createTestProvider("Happy Paws", "555-0101"));

        MockHttpServletResponse response = mockMvc.perform(get("/providers"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Provider> providers = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<Provider>>() {}
        );

        assertTrue(providers.size() > 0);
    }

    @Test
    void getProviderById() throws Exception {
        Provider saved = providerRepository.save(createTestProvider("City Shelter", "555-0102"));

        MockHttpServletResponse response = mockMvc.perform(get("/providers/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(saved.getId(), receivedJson.get("id").asInt());
        assertEquals("City Shelter", receivedJson.get("orgName").asText());
    }

    @Test
    void getProviderByUserId() throws Exception {
        providerRepository.save(createTestProvider("Rescue Me", "555-0103"));

        MockHttpServletResponse response = mockMvc.perform(get("/providers/by-user/" + testUser.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(testUser.getId(), receivedJson.get("userId").asInt());
        assertEquals("Rescue Me", receivedJson.get("orgName").asText());
    }

    @Test
    void createProvider() throws Exception {
        ObjectNode providerJson = objectMapper.createObjectNode();
        providerJson.put("userId", testUser.getId());
        providerJson.put("orgName", "New Hope Rescue");
        providerJson.put("phone", "555-9999");
        providerJson.put("address", "456 Kindness Blvd");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/providers")
                                .contentType("application/json")
                                .content(providerJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Provider returnedProvider = objectMapper.readValue(response.getContentAsString(), Provider.class);
        assertNotNull(returnedProvider.getId());

        Provider savedInDb = providerRepository.findById(returnedProvider.getId()).get();
        assertEquals("New Hope Rescue", savedInDb.getOrgName());
    }

    @Test
    void updateProvider() throws Exception {
        Provider saved = providerRepository.save(createTestProvider("Old Name", "555-0000"));

        ObjectNode updatedJson = objectMapper.createObjectNode();
        updatedJson.put("orgName", "Updated Name");
        updatedJson.put("phone", "555-1111");
        updatedJson.put("address", "Updated Address");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/providers/" + saved.getId())
                                .contentType("application/json")
                                .content(updatedJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Provider updatedProvider = providerRepository.findById(saved.getId()).get();
        assertEquals("Updated Name", updatedProvider.getOrgName());
        assertEquals("555-1111", updatedProvider.getPhone());
    }

    @Test
    void deleteProvider() throws Exception {
        Provider saved = providerRepository.save(createTestProvider("Temporary Shelter", "555-8888"));

        assertTrue(providerRepository.findById(saved.getId()).isPresent());

        MockHttpServletResponse response = mockMvc.perform(delete("/providers/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertFalse(providerRepository.findById(saved.getId()).isPresent());
    }
}