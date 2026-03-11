package com.pawfectmatch.backend.adopters;

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
@RequestMapping("/adopters")
public class AdopterController {

    private final AdopterRepository repo;

    public AdopterController(AdopterRepository repo) {
        this.repo = repo;
    }

    // GET /adopters — return all adopters
    @GetMapping
    public List<Adopter> getAllAdopters() {
        return repo.findAll();
    }

    // GET /adopters/{id} — return one adopter by id
    @GetMapping("/{id}")
    public ResponseEntity<Adopter> getAdopterById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /adopters — create a new adopter profile
    @PostMapping
    public Adopter createAdopter(@RequestBody Adopter adopter) {
        return repo.save(adopter);
    }

    // PUT /adopters/{id} — update an existing adopter profile
    @PutMapping("/{id}")
    public ResponseEntity<Adopter> updateAdopter(@PathVariable Integer id, @RequestBody Adopter updated) {
        return repo.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setPhone(updated.getPhone());
            existing.setLivingEnvironment(updated.getLivingEnvironment());
            existing.setHouseholdSize(updated.getHouseholdSize());
            existing.setHasChildren(updated.getHasChildren());
            existing.setHasOtherPets(updated.getHasOtherPets());
            existing.setPreferredSpecies(updated.getPreferredSpecies());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /adopters/{id} — delete an adopter profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdopter(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
