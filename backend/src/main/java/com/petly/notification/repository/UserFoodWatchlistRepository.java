package com.petly.notification.repository;

import com.petly.notification.entity.UserFoodWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
public interface UserFoodWatchlistRepository
        extends JpaRepository<UserFoodWatchlist, Long> {

    List<UserFoodWatchlist> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Optional<UserFoodWatchlist> findByUserIdAndFoodProductId(
            Long userId,
            Long foodProductId
    );

    boolean existsByUserIdAndFoodProductId(
            Long userId,
            Long foodProductId
    );

    void deleteByUserIdAndFoodProductId(
            Long userId,
            Long foodProductId
    );
}

