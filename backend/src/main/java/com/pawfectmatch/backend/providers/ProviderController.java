package com.pawfectmatch.backend.providers;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderRepository repo;

    public ProviderController(ProviderRepository repo) {
        this.repo = repo;
    }

    // GET /providers → returns all providers (shelters/rescues)
    @GetMapping
    public List<Provider> getAllProviders() {
        return repo.findAll();
    }

    // GET /providers/1 → returns one provider by provider ID
    @GetMapping("/{id}")
    public Provider getProviderById(@PathVariable Integer id) {
        return repo.findById(id).get();
    }

    // GET /providers/by-user/1 → returns provider profile by their user ID
    // Use this on the frontend when you have the logged-in user's ID
    @GetMapping("/by-user/{userId}")
    public Provider getProviderByUserId(@PathVariable Integer userId) {
        List<Provider> all = repo.findAll();
        for (Provider p : all) {
            if (p.getUserId().equals(userId)) {
                return p;
            }
        }
        return null;
    }

    // POST /providers → create a new provider profile
    // Body: { "userId": 1, "orgName": "Happy Paws", "phone": "555-0101", "address": "...", ... }
    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return repo.save(provider);
    }

    // PUT /providers/1 → update a provider profile
    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable Integer id, @RequestBody Provider updated) {
        Provider existing = repo.findById(id).get();
        existing.setOrgName(updated.getOrgName());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setDescription(updated.getDescription());
        existing.setFoundedYear(updated.getFoundedYear());
        return repo.save(existing);
    }

    // DELETE /providers/1 → delete a provider
    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}