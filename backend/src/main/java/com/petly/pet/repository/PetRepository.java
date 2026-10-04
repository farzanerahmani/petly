package com.petly.pet.repository;

import com.petly.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
public interface PetRepository extends JpaRepository<Pet, Long> {
}
