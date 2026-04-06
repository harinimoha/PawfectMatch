package com.pawfectmatch.backend.pets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PetImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg");

    private final Path uploadDirectory;

    public PetImageStorageService(@Value("${app.pet-image-upload-dir:uploads/pet-images}") String uploadDir) {
        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Only PNG and JPEG images are allowed");
        }

        Files.createDirectories(uploadDirectory);

        String extension = contentType.equalsIgnoreCase("image/png") ? ".png" : ".jpg";
        String filename = UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(filename);

        image.transferTo(destination);

        return "/pet-images/" + filename;
    }

    public Resource loadAsResource(String filename) throws IOException {
        Path file = uploadDirectory.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("Image not found: " + filename);
        }

        return resource;
    }
}