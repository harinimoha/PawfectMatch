package com.pawfectmatch.backend;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import com.pawfectmatch.backend.adopters.Adopter;
import com.pawfectmatch.backend.adopters.AdopterRepository;
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
public class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        //
        // Creating a parent user to satisfy the FK constraint
        User user = new User();
        user.setEmail("test" + System.currentTimeMillis() + "@example.com");
        user.setPassword("password123");
        user.setRole("ADOPTER");
        testUser = userRepository.save(user);
    }

    // Helper method: creating a valid adopter with all mandatory fields
    private Adopter createTestAdopter(String fName, String lName, String phone) {
        Adopter adopter = new Adopter();
        adopter.setUserId(testUser.getId());
        adopter.setFirstName(fName);
        adopter.setLastName(lName);
        adopter.setPhone(phone);
        return adopter;
    }

    @Test
    void getAllAdopters() throws Exception {
        adopterRepository.save(createTestAdopter("Alice", "Tan", "555-0001"));

        MockHttpServletResponse response = mockMvc.perform(get("/adopters"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Adopter> adopters = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<Adopter>>() {}
        );

        assertTrue(adopters.size() > 0);
    }

    @Test
    void getAdopterById() throws Exception {
        Adopter adopter = createTestAdopter("Alice", "Tan", "555-1001");
        adopter.setLivingEnvironment("Apartment");
        Adopter saved = adopterRepository.save(adopter);

        MockHttpServletResponse response = mockMvc.perform(get("/adopters/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(saved.getId(), receivedJson.get("id").asInt());
        assertEquals("Alice", receivedJson.get("firstName").asText());
    }

    @Test
    void getAdopterByUserId() throws Exception {
        Adopter adopter = createTestAdopter("Bob", "Lee", "555-2002");
        adopterRepository.save(adopter);

        MockHttpServletResponse response = mockMvc.perform(get("/adopters/by-user/" + testUser.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals("Bob", receivedJson.get("firstName").asText());
        assertEquals(testUser.getId(), receivedJson.get("userId").asInt());
    }

    @Test
    void createAdopter() throws Exception {
        ObjectNode adopterJson = objectMapper.createObjectNode();
        adopterJson.put("userId", testUser.getId());
        adopterJson.put("firstName", "David");
        adopterJson.put("lastName", "Kim");
        adopterJson.put("phone", "555-4004");
        adopterJson.put("livingEnvironment", "Townhouse");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/adopters")
                                .contentType("application/json")
                                .content(adopterJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Adopter returnedAdopter = objectMapper.readValue(response.getContentAsString(), Adopter.class);
        assertNotNull(returnedAdopter.getId());
        assertEquals("David", returnedAdopter.getFirstName());
    }

    @Test
    void updateAdopter() throws Exception {
        Adopter saved = adopterRepository.save(createTestAdopter("Emma", "Stone", "555-5005"));

        ObjectNode updatedJson = objectMapper.createObjectNode();
        updatedJson.put("firstName", "Emily");
        updatedJson.put("lastName", "Stone");
        updatedJson.put("phone", "555-9999");
        updatedJson.put("livingEnvironment", "House");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/adopters/" + saved.getId())
                                .contentType("application/json")
                                .content(updatedJson.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Adopter updatedAdopter = adopterRepository.findById(saved.getId()).get();
        assertEquals("Emily", updatedAdopter.getFirstName());
        assertEquals("555-9999", updatedAdopter.getPhone());
    }

    @Test
    void deleteAdopter() throws Exception {
        Adopter saved = adopterRepository.save(createTestAdopter("Frank", "Moore", "555-6006"));

        assertTrue(adopterRepository.findById(saved.getId()).isPresent());

        MockHttpServletResponse response = mockMvc.perform(delete("/adopters/" + saved.getId()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertFalse(adopterRepository.findById(saved.getId()).isPresent());
    }
}