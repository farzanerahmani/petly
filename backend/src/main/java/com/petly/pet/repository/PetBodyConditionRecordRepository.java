package com.petly.pet.repository;

import com.petly.pet.entity.PetBodyConditionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
public interface PetBodyConditionRecordRepository extends JpaRepository<PetBodyConditionRecord, Long> {

    List<PetBodyConditionRecord> findByPetIdOrderByRecordedDateDesc(Long petId);
}
