package com.pawfectmatch.backend.users;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // GET /users → returns all users
    @GetMapping
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // GET /users/1 → returns one user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return repo.findById(id).get();
    }

    // POST /users → create a new user
    // Body: { "email": "alice@example.com", "password": "pass123", "role": "ADOPTER" }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return repo.save(user);
    }

    // PUT /users/1 → update an existing user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updated) {
        User existing = repo.findById(id).get();
        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());
        existing.setRole(updated.getRole());
        return repo.save(existing);
    }

    // DELETE /users/1 → delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}