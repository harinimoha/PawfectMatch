package com.pawfectmatch.backend.pets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Entity for pet adoption posts.
@Entity
@Table(name = "petposts")
public class PetPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;
    private String species;
    private String breed;
    private Integer age;
    private String temperament;
    private String description;
    private String imageUrl;
    private String providerName;

    // Default constructor required by JPA.
    public PetPost() {
    }

    // Constructor for creating pet posts.
    public PetPost(
        String petName,
        String species,
        String breed,
        Integer age,
        String temperament,
        String description,
        String imageUrl,
        String providerName
    ) {
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.temperament = temperament;
        this.description = description;
        this.imageUrl = imageUrl;
        this.providerName = providerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}