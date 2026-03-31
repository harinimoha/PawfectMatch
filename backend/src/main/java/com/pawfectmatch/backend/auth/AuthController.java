package com.pawfectmatch.backend.auth;

import com.pawfectmatch.backend.users.User;
import com.pawfectmatch.backend.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        List<User> allUsers = userRepo.findAll();
        
        for (User user : allUsers) {
            if (user.getEmail().equals(request.getEmail()) &&
                user.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
               .body("Invalid email or password");
    }
}