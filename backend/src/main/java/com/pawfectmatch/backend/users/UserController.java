package com.pawfectmatch.backend.users;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADOPTER", "PROVIDER");

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
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // POST /users → create a new user
    // Body: { "email": "alice@example.com", "password": "pass123", "role":
    // "ADOPTER" }
    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            normalizeAndValidate(user);
            return repo.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists or user data violates constraints");
        }
    }

    // PUT /users/1 → update an existing user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updated) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        normalizeAndValidate(updated);

        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());
        existing.setRole(updated.getRole());

        try {
            return repo.save(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists or user data violates constraints");
        }
    }

    // DELETE /users/1 → delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        repo.deleteById(id);
    }

    private void normalizeAndValidate(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());
        user.setRole(user.getRole().trim().toUpperCase(Locale.ROOT));

        if (!ALLOWED_ROLES.contains(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role must be ADOPTER or PROVIDER");
        }
    }
}