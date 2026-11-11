package com.petly.notification.service;

import com.petly.auth.service.CurrentUserService;
import com.petly.food.repository.FoodProductRepository;
import com.petly.notification.dto.FoodWatchlistResponse;
import com.petly.notification.entity.UserFoodWatchlist;
import com.petly.notification.repository.UserFoodWatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
@Service
public class FoodWatchlistService {

    private final CurrentUserService currentUserService;
    private final UserFoodWatchlistRepository watchlistRepository;
    private final FoodProductRepository foodProductRepository;

    public FoodWatchlistService(
            CurrentUserService currentUserService,
            UserFoodWatchlistRepository watchlistRepository,
            FoodProductRepository foodProductRepository
    ) {
        this.currentUserService = currentUserService;
        this.watchlistRepository = watchlistRepository;
        this.foodProductRepository = foodProductRepository;
    }

    public List<FoodWatchlistResponse> getMyWatchlist() {
        Long userId = currentUserService.getCurrentUserId();

        return watchlistRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(FoodWatchlistResponse::from)
                .toList();
    }

    @Transactional
    public FoodWatchlistResponse addFood(Long foodProductId) {
        if (foodProductId == null || foodProductId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid food product ID"
            );
        }

        Long userId = currentUserService.getCurrentUserId();

        if (!foodProductRepository.existsById(foodProductId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Food product not found"
            );
        }

        var existing = watchlistRepository
                .findByUserIdAndFoodProductId(userId, foodProductId);

        if (existing.isPresent()) {
            return FoodWatchlistResponse.from(existing.get());
        }

        UserFoodWatchlist item = new UserFoodWatchlist();
        item.setUserId(userId);
        item.setFoodProductId(foodProductId);

        return FoodWatchlistResponse.from(
                watchlistRepository.save(item)
        );
    }

    @Transactional
    public void removeFood(Long foodProductId) {
        Long userId = currentUserService.getCurrentUserId();

        if (!watchlistRepository.existsByUserIdAndFoodProductId(
                userId, foodProductId
        )) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Food not found in your watchlist"
            );
        }

        watchlistRepository.deleteByUserIdAndFoodProductId(
                userId, foodProductId
        );
    }
}

