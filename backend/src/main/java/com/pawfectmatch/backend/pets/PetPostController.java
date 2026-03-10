package com.pawfectmatch.backend.pets;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// REST controller for PetPost CRUD endpoints.
@RestController
@CrossOrigin
public class PetPostController {
    private final PetPostRepository repo;

    public PetPostController(PetPostRepository repo) {
        this.repo = repo;
    }

    // Returns all pet posts.
    @GetMapping("/petposts")
    public List<PetPost> getAllPetPosts() {
        return repo.findAll();
    }

    // Creates a new pet post.
    @PostMapping("/petposts")
    public PetPost createPetPost(@RequestBody PetPost petPost) {
        return repo.save(petPost);
    }

    // Updates a pet post by id.
    @PutMapping("/petposts/{id}")
    public PetPost updatePetPost(@PathVariable Long id, @RequestBody PetPost updatedPetPost) {
        PetPost petPost = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("PetPost not found: " + id));

        petPost.setPetName(updatedPetPost.getPetName());
        petPost.setSpecies(updatedPetPost.getSpecies());
        petPost.setBreed(updatedPetPost.getBreed());
        petPost.setAge(updatedPetPost.getAge());
        petPost.setTemperament(updatedPetPost.getTemperament());
        petPost.setDescription(updatedPetPost.getDescription());
        petPost.setImageUrl(updatedPetPost.getImageUrl());
        petPost.setProviderName(updatedPetPost.getProviderName());

        return repo.save(petPost);
    }

    // Deletes a pet post by id.
    @DeleteMapping("/petposts/{id}")
    public void deletePetPost(@PathVariable Long id) {
        repo.deleteById(id);
    }
}