package com.pawfectmatch.backend.adopters;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adopters")
public class AdopterController {

    private final AdopterRepository repo;

    public AdopterController(AdopterRepository repo) {
        this.repo = repo;
    }

    // GET /adopters → returns all adopter profiles
    @GetMapping
    public List<Adopter> getAllAdopters() {
        return repo.findAll();
    }

    // GET /adopters/1 → returns one adopter by adopter ID
    @GetMapping("/{id}")
    public Adopter getAdopterById(@PathVariable Integer id) {
        return repo.findById(id).get();
    }

    // GET /adopters/by-user/5 → returns adopter profile by their user ID
    // Use this on the frontend when you have the logged-in user's ID
    @GetMapping("/by-user/{userId}")
    public Adopter getAdopterByUserId(@PathVariable Integer userId) {
        List<Adopter> all = repo.findAll();
        for (Adopter a : all) {
            if (a.getUserId().equals(userId)) {
                return a;
            }
        }
        return null;
    }

    // POST /adopters → create a new adopter profile
    // Body: { "userId": 5, "firstName": "Alice", "lastName": "Tan", "phone": "555-1001", ... }
    @PostMapping
    public Adopter createAdopter(@RequestBody Adopter adopter) {
        return repo.save(adopter);
    }

    // PUT /adopters/1 → update an adopter profile
    @PutMapping("/{id}")
    public Adopter updateAdopter(@PathVariable Integer id, @RequestBody Adopter updated) {
        Adopter existing = repo.findById(id).get();
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setLivingEnvironment(updated.getLivingEnvironment());
        existing.setHouseholdSize(updated.getHouseholdSize());
        existing.setHasChildren(updated.getHasChildren());
        existing.setHasOtherPets(updated.getHasOtherPets());
        existing.setPreferredSpecies(updated.getPreferredSpecies());
        return repo.save(existing);
    }

    // DELETE /adopters/1 → delete an adopter profile
    @DeleteMapping("/{id}")
    public void deleteAdopter(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}