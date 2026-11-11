package com.petly.food.repository;

import com.petly.food.entity.StoreOwnerRequest;
import com.petly.food.entity.enums.StoreOwnerRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
public interface StoreOwnerRequestRepository
        extends JpaRepository<StoreOwnerRequest, Long> {

    List<StoreOwnerRequest> findAllByStatusOrderByCreatedAtDesc(
            StoreOwnerRequestStatus status
    );

    List<StoreOwnerRequest> findAllByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Optional<StoreOwnerRequest> findByIdAndUserId(
            Long requestId,
            Long userId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM StoreOwnerRequest r
        WHERE r.id = :requestId
          AND r.status = :status
    """)
    Optional<StoreOwnerRequest> findByIdAndStatusForUpdate(
            @Param("requestId") Long requestId,
            @Param("status") StoreOwnerRequestStatus status
    );


    boolean existsByUserIdAndShopNameIgnoreCaseAndCityIgnoreCaseAndStatus(
            Long userId,
            String shopName,
            String city,
            StoreOwnerRequestStatus status
    );
}


