package com.pawfectmatch.backend.pets;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pet-images")
public class PetImageController {

    private final PetRepository petRepository;
    private final PetImageStorageService storageService;

    public PetImageController(PetRepository petRepository, PetImageStorageService storageService) {
        this.petRepository = petRepository;
        this.storageService = storageService;
    }

    @PostMapping("/pets/{petId}")
    public ResponseEntity<Pet> uploadPetImage(@PathVariable Integer petId, @RequestParam("image") MultipartFile image)
            throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found: " + petId));

        String photoUrl;
        try {
            photoUrl = storageService.store(image);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }

        pet.setPhotoUrl(photoUrl);
        Pet savedPet = petRepository.save(pet);

        return ResponseEntity.ok(savedPet);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getPetImage(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);

        MediaType mediaType = filename.toLowerCase().endsWith(".png")
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                .body(resource);
    }
}