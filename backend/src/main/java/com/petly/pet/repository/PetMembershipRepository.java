package com.petly.pet.repository;

import com.petly.pet.entity.PetMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
public interface PetMembershipRepository extends JpaRepository<PetMembership,Long> {

    List<PetMembership> findAllByUserId(Long userId);

    List<PetMembership> findAllByPetId(Long petId);

    Optional<PetMembership> findAllByUserIdAndPetId(Long userId,Long petId);

    boolean existsByUserIdAndPetId(Long userId,Long petId);
}
