package com.petly.food.controller;

import com.petly.food.controller.dto.FoodScrapingBatchResponse;
import com.petly.food.controller.dto.FoodScrapingRequest;
import com.petly.food.service.FoodScrapingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@RestController
@Validated
@RequestMapping("/api/v1/scraper")
public class FoodScrapingController {

    private final FoodScrapingService foodScrapingService;

    public FoodScrapingController(
            FoodScrapingService foodScrapingService
    ) {
        this.foodScrapingService = foodScrapingService;
    }


    @PostMapping("/food-products")
    public ResponseEntity<FoodScrapingBatchResponse> receiveFoodProducts(
            @RequestParam
            @NotBlank
            @Size(max = 500)
            String shopWebsiteUrl,
            @Valid @RequestBody List<@Valid FoodScrapingRequest> requests
    ) {
        FoodScrapingBatchResponse response =
                foodScrapingService.processBatch(shopWebsiteUrl, requests);

        return ResponseEntity.ok(response);
    }

}

