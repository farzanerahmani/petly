package com.petly.pet.repository;

import com.petly.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
public interface PetRepository extends JpaRepository<Pet,Long> {
}
