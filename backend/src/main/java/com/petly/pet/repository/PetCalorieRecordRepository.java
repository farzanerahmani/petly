package com.petly.pet.repository;

import com.petly.pet.entity.PetCalorieRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/18/2026
 */
public interface PetCalorieRecordRepository extends JpaRepository<PetCalorieRecord, Long> {

    List<PetCalorieRecord> findAllByPet_IdOrderByCreatedAtDesc(Long petId);

    Optional<PetCalorieRecord> findFirstByPet_IdOrderByCreatedAtDesc(Long petId);
}
