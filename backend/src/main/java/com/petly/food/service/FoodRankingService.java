package com.petly.food.service;

import com.petly.food.controller.dto.FoodProductResponseDto;
import com.petly.food.controller.dto.FoodRecommendationResponseDto;
import com.petly.food.entity.FoodProduct;
import com.petly.food.entity.FoodNutrition;
import com.petly.food.entity.enums.FoodLifeStage;
import com.petly.pet.entity.Pet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@Service
public class FoodRankingService {

    public List<FoodRecommendationResponseDto.RecommendationItem> rank(
            Pet pet,
            FoodLifeStage expectedLifeStage,
            List<FoodProduct> products
    ) {
        return products.stream()
                .map(product -> createItem(product, expectedLifeStage))
                .sorted(
                        Comparator
                                .comparingInt(
                                        FoodRecommendationResponseDto
                                                .RecommendationItem::getScore
                                )
                                .reversed()
                                .thenComparing(
                                        item -> item.getFood().getName(),
                                        String.CASE_INSENSITIVE_ORDER
                                )
                )
                .toList();
    }

    private FoodRecommendationResponseDto.RecommendationItem createItem(
            FoodProduct product,
            FoodLifeStage expectedLifeStage
    ) {
        var item =
                new FoodRecommendationResponseDto.RecommendationItem();

        item.setFood(FoodProductResponseDto.fromEntity(product));

        int score = 0;
        List<String> reasons = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        FoodLifeStage productStage = product.getLifeStage();

        if (expectedLifeStage == null) {
            reasons.add(
                    "مرحله زندگی پت به‌اندازه کافی مشخص نیست؛ سازگاری سنی قطعی نیست."
            );
        } else if (productStage == FoodLifeStage.ALL_LIFE_STAGES) {
            score += 25;
            reasons.add("محصول برای همه مراحل زندگی معرفی شده است.");
        } else if (productStage == expectedLifeStage) {
            score += 40;
            reasons.add("مرحله زندگی محصول با مرحله تخمینی پت مطابقت دارد.");
        } else if (productStage == null) {
            score += 5;
            warnings.add("مرحله زندگی مناسب این محصول مشخص نشده است.");
        } else {
            warnings.add(
                    "مرحله زندگی محصول با مرحله تخمینی پت مطابقت ندارد."
            );
        }

        FoodNutrition nutrition = product.getNutrition();

        if (nutrition == null) {
            warnings.add("اطلاعات تغذیه‌ای محصول ثبت نشده است.");
        } else {
            if (nutrition.getEnergyKcalPer100g() != null) {
                score += 15;
                reasons.add("اطلاعات انرژی محصول موجود است.");
            } else {
                warnings.add("انرژی محصول ثبت نشده است.");
            }

            if (nutrition.getProteinPercent() != null) {
                score += 10;
                reasons.add("اطلاعات پروتئین محصول موجود است.");
            } else {
                warnings.add("درصد پروتئین محصول مشخص نیست.");
            }

            if (nutrition.getFatPercent() != null) {
                score += 5;
                reasons.add("اطلاعات چربی محصول موجود است.");
            } else {
                warnings.add("درصد چربی محصول مشخص نیست.");
            }

            if (nutrition.getVerifiedAt() != null) {
                score += 10;
                reasons.add("اطلاعات تغذیه‌ای دارای زمان تأیید است.");
            } else {
                warnings.add(
                        "زمان تأیید اطلاعات تغذیه‌ای ثبت نشده است."
                );
            }
        }

        if (reasons.isEmpty()) {
            reasons.add("اطلاعات کافی برای تأیید سازگاری این محصول وجود ندارد.");
        }

        item.setScore(score);
        item.setReasons(reasons);
        item.setWarnings(warnings);

        return item;
    }
}
