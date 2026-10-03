package com.petly.pet.repository;

import com.petly.pet.entity.PetWeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
public interface PetWeightRecordRepository extends JpaRepository<PetWeightRecord, Long> {

    List<PetWeightRecord> findByPetIdOrderByRecordedDateDesc(Long petId);
}
