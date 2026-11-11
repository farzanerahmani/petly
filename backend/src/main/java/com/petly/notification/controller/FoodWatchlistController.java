package com.petly.notification.controller;

import com.petly.notification.dto.AddFoodWatchlistRequest;
import com.petly.notification.dto.FoodWatchlistResponse;
import com.petly.notification.service.FoodWatchlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
@RestController
@RequestMapping("/api/v1/users/me/food-watchlist")
public class FoodWatchlistController {

    private final FoodWatchlistService foodWatchlistService;

    public FoodWatchlistController(
            FoodWatchlistService foodWatchlistService
    ) {
        this.foodWatchlistService = foodWatchlistService;
    }

    @GetMapping
    public List<FoodWatchlistResponse> getMyWatchlist() {
        return foodWatchlistService.getMyWatchlist();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodWatchlistResponse addFood(
            @Valid @RequestBody AddFoodWatchlistRequest request
    ) {
        return foodWatchlistService.addFood(
                request.foodProductId()
        );
    }

    @DeleteMapping("/{foodProductId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFood(
            @PathVariable Long foodProductId
    ) {
        foodWatchlistService.removeFood(foodProductId);
    }
}

