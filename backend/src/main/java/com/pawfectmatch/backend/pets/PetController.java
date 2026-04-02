package com.pawfectmatch.backend.pets;
import com.pawfectmatch.backend.messages.MessageRepository;


import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetRepository repo;
    private final MessageRepository messageRepo;
    
    public PetController(PetRepository repo, MessageRepository messageRepo) {
        this.repo = repo;
        this.messageRepo = messageRepo;
    }

    // GET /pets → returns all pets
    @GetMapping
    public List<Pet> getAllPets() {
        return repo.findAll();
    }

    // GET /pets/1 → returns one pet by ID
    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable Integer id) {
        return repo.findById(id).get();
    }

    // GET /pets/available → returns only pets with status AVAILABLE
    @GetMapping("/available")
    public List<Pet> getAvailablePets() {
        List<Pet> all = repo.findAll();
        List<Pet> available = new ArrayList<>();
        for (Pet p : all) {
            if ("AVAILABLE".equals(p.getStatus())) {
                available.add(p);
            }
        }
        return available;
    }

    // GET /pets/by-provider/1 → returns all pets from a specific shelter
    @GetMapping("/by-provider/{providerId}")
    public List<Pet> getPetsByProvider(@PathVariable Integer providerId) {
        List<Pet> all = repo.findAll();
        List<Pet> result = new ArrayList<>();
        for (Pet p : all) {
            if (p.getProviderId().equals(providerId)) {
                result.add(p);
            }
        }
        return result;
    }

    // POST /pets → add a new pet listing
    // Body: { "providerId": 1, "name": "Milo", "species": "Dog", "status": "AVAILABLE", ... }
    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        return repo.save(pet);
    }

    // PUT /pets/1 → update a pet listing
    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Integer id, @RequestBody Pet updated) {
        Pet existing = repo.findById(id).get();
        existing.setName(updated.getName());
        existing.setSpecies(updated.getSpecies());
        existing.setBreed(updated.getBreed());
        existing.setAge(updated.getAge());
        existing.setTemperament(updated.getTemperament());
        existing.setDescription(updated.getDescription());
        existing.setPhotoUrl(updated.getPhotoUrl());
        existing.setStatus(updated.getStatus());
        return repo.save(existing);
    }

    // DELETE /pets/1 → delete a pet listing
    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Integer id) {
        messageRepo.deleteByPetId(id);
        repo.deleteById(id);
    }
}