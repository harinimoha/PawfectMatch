package com.pawfectmatch.backend.pets;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetRepository repo;

    // Spring automatically injects the repository here
    public PetController(PetRepository repo) {
        this.repo = repo;
    }

    // GET /pets — return all pets
    @GetMapping
    public List<Pet> getAllPets() {
        return repo.findAll();
    }

    // GET /pets/{id} — return one pet by id
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /pets — create a new pet
    // The request body (JSON) is automatically converted into a Pet object
    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        return repo.save(pet);
    }

    // PUT /pets/{id} — update an existing pet
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Integer id, @RequestBody Pet updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setSpecies(updated.getSpecies());
            existing.setBreed(updated.getBreed());
            existing.setAge(updated.getAge());
            existing.setTemperament(updated.getTemperament());
            existing.setDescription(updated.getDescription());
            existing.setPhotoUrl(updated.getPhotoUrl());
            existing.setStatus(updated.getStatus());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /pets/{id} — delete a pet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}