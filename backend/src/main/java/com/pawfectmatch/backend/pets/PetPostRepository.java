package com.pawfectmatch.backend.pets;

import org.springframework.data.jpa.repository.JpaRepository;

// Repository for PetPost persistence operations.
public interface PetPostRepository extends JpaRepository<PetPost, Long> {}
