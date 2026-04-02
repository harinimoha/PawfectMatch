package com.pawfectmatch.backend.messages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("DELETE FROM Message m WHERE m.petId = :petId")
    @Modifying
    @Transactional
    void deleteByPetId(@Param("petId") Integer petId);

}