package com.pawfectmatch.backend.pets;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetController {
    private final PetRepository repo;

    public PetController(PetRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/pets")
    public List<Pet> getAllPets() {
        return repo.findAll();
    }
}