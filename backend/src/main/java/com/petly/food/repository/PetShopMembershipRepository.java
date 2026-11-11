package com.petly.food.repository;

import com.petly.food.entity.PetShopMembership;
import com.petly.food.entity.enums.PetShopMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
public interface PetShopMembershipRepository extends JpaRepository<PetShopMembership, Long> {

    List<PetShopMembership> findAllByUserIdAndActiveTrue(Long userId);

    List<PetShopMembership> findAllByPetShopIdAndActiveTrue(Long petShopId);

    Optional<PetShopMembership> findByUserIdAndPetShopIdAndActiveTrue(Long userId, Long petShopId);

    boolean existsByPetShopIdAndRoleAndActiveTrue(Long petShopId, PetShopMemberRole role);

    Optional<PetShopMembership> findByUserIdAndPetShopId(Long userId, Long petShopId);


    Optional<PetShopMembership> findByPetShopIdAndUserPhoneNumber(
            Long petShopId,
            String phoneNumber
    );

    List<PetShopMembership> findAllByPetShopIdOrderByCreatedAtDesc(
            Long petShopId
    );

    List<PetShopMembership> findAllByPetShopIdAndActiveTrueOrderByCreatedAtDesc(
            Long petShopId
    );

}

