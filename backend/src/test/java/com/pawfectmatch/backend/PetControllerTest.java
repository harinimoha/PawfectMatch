package com.pawfectmatch.backend;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import com.pawfectmatch.backend.pets.Pet;
import com.pawfectmatch.backend.pets.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetRepository petRepository;

    // GET /pets
    @Test
    void getAllPets() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/pets"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Pet> pets = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Pet>>() {});
        assertTrue(pets.size() > 0);
    }

    // GET /pets/{id}
    @Test
    void getPetById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/pets/1"))  // "Milo"
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // Verify Milo from data.sql
        assertTrue(petRepository.findById(1).isPresent());
        Pet milo = petRepository.findById(1).get();

        assertEquals("Milo", milo.getName());
        assertEquals("Dog", milo.getSpecies());
        assertEquals("Golden Retriever", milo.getBreed());
        assertEquals("AVAILABLE", milo.getStatus());
        assertEquals(1, milo.getProviderId());
    }

    @Test
    void getPetById_nonExistent() throws Exception {
        // Pet 9999 does not exist 
        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/pets/9999")).andReturn()
        );
        assertFalse(petRepository.findById(9999).isPresent());
    }


    // GET /pets/available
    @Test
    void getAvailablePets() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/pets/available"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Pet> available = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Pet>>() {});
        assertTrue(available.size() > 0);

        // Every returned pet must have status AVAILABLE
        for (Pet pet : available) {
            assertEquals("AVAILABLE", pet.getStatus());
        }
    }

    // GET /pets/by-provider/{providerId}
    @Test
    void getPetsByProviderId_Provider1() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/pets/by-provider/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Pet> pets = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Pet>>() {});
        assertTrue(pets.size() > 0);

        for (Pet pet : pets) {
            assertEquals(1, pet.getProviderId());
        }
    }

    @Test
    void getPetsByProviderId_nonExistentProvider() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/pets/by-provider/999"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Pet> pets = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Pet>>() {});
        assertEquals(0, pets.size());
    }

    // POST /pets
    @Test
    void createPet() throws Exception {
        ObjectNode petJSON = objectMapper.createObjectNode();
        petJSON.put("providerId", 1);
        petJSON.put("name", "Ziggy");
        petJSON.put("species", "Dog");
        petJSON.put("breed", "Husky");
        petJSON.put("age", 2);
        petJSON.put("temperament", "Energetic, Friendly");
        petJSON.put("description", "Loves the snow.");
        petJSON.put("status", "AVAILABLE");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/pets")
                                .contentType("application/json")
                                .content(petJSON.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // get the auto-generated ID
        Pet returnedPet = objectMapper.readValue(response.getContentAsString(), Pet.class);
        Integer newId = returnedPet.getId();

        // verify the pet exists in DB with correct details
        assertTrue(petRepository.findById(newId).isPresent());
        Pet savedPet = petRepository.findById(newId).get();

        assertEquals("Ziggy", savedPet.getName());
        assertEquals("Dog", savedPet.getSpecies());
        assertEquals("Husky", savedPet.getBreed());
        assertEquals(2, savedPet.getAge());
        assertEquals("AVAILABLE", savedPet.getStatus());
        assertEquals(1, savedPet.getProviderId());
    }

    // PUT /pets/{id}
    // changes name and status
    @Test
    void updatePet() throws Exception {
        ObjectNode updatedPetJSON = objectMapper.createObjectNode();
        updatedPetJSON.put("name", "Luna Updated");
        updatedPetJSON.put("species", "Cat");
        updatedPetJSON.put("breed", "Domestic Shorthair");
        updatedPetJSON.put("age", 3);
        updatedPetJSON.put("temperament", "Calm, Affectionate");
        updatedPetJSON.put("description", "Updated description.");
        updatedPetJSON.put("status", "ON_HOLD");

        MockHttpServletResponse response = mockMvc.perform(
                        put("/pets/2")
                                .contentType("application/json")
                                .content(updatedPetJSON.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // Verify the update persisted in DB
        assertTrue(petRepository.findById(2).isPresent());
        Pet updatedPet = petRepository.findById(2).get();

        assertEquals("Luna Updated", updatedPet.getName());
        assertEquals("ON_HOLD", updatedPet.getStatus());
        assertEquals(3, updatedPet.getAge());
    }

    // DELETE /pets/{id}
    @Test
    void deletePet() throws Exception {
        // confirm pet 15 (Nala) exists
        assertTrue(petRepository.findById(15).isPresent());

        MockHttpServletResponse response = mockMvc.perform(delete("/pets/15"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // verify pet 15 is deleted
        assertFalse(petRepository.findById(15).isPresent());
    }

    // DELETE non-existent ID
    @Test
    void deletePet_nonExistent() throws Exception {
        // Pet 9999 does not exist 
        MockHttpServletResponse response = mockMvc.perform(delete("/pets/9999"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }
}
