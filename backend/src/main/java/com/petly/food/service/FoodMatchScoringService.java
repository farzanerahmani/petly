package com.petly.food.service;

import com.petly.food.entity.FoodProduct;
import com.petly.food.entity.FoodVariant;
import com.petly.food.entity.enums.WeightStatus;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public class FoodMatchScoringService {

    public MatchScore score(
            FoodProduct source,
            FoodVariant sourceVariant,
            FoodProduct target,
            FoodVariant targetVariant
    ) {
        if (source == null || sourceVariant == null
                || target == null || targetVariant == null) {
            return MatchScore.rejected("اطلاعات محصول یا بسته‌بندی ناقص است.");
        }

        if (source.getSpecies() != target.getSpecies()) {
            return MatchScore.rejected("گونه حیوان متفاوت است.");
        }

        if (source.getFoodType() != target.getFoodType()) {
            return MatchScore.rejected("نوع غذا متفاوت است.");
        }

        Integer sourceWeight = sourceVariant.getPackageWeightGrams();
        Integer targetWeight = targetVariant.getPackageWeightGrams();

        boolean sourceConfirmed =
                sourceVariant.getWeightStatus() == WeightStatus.CONFIRMED
                        && sourceWeight != null;

        boolean targetConfirmed =
                targetVariant.getWeightStatus() == WeightStatus.CONFIRMED
                        && targetWeight != null;

        if (sourceConfirmed && targetConfirmed
                && !sourceWeight.equals(targetWeight)) {
            return MatchScore.rejected(
                    "وزن بسته‌بندی‌ها متفاوت است."
            );
        }

        double score = 0;
        StringBuilder reason = new StringBuilder();

        double nameSimilarity = similarity(
                source.getName(),
                target.getName()
        );

        score += nameSimilarity * 40;
        reason.append("شباهت نام: ")
                .append(Math.round(nameSimilarity * 100))
                .append("%؛ ");

        String sourceBrand = normalize(source.getBrand());
        String targetBrand = normalize(target.getBrand());

        if (!sourceBrand.isBlank() && !targetBrand.isBlank()) {
            if (sourceBrand.equals(targetBrand)) {
                score += 25;
                reason.append("برند یکسان؛ ");
            } else {
                reason.append("برند متفاوت؛ ");
            }
        } else {
            reason.append("اطلاعات برند ناقص؛ ");
        }

        if (sourceConfirmed && targetConfirmed) {
            score += 25;
            reason.append("وزن تأییدشده و یکسان؛ ");
        } else {
            score += 5;
            reason.append("وزن نیازمند بررسی یا نامشخص؛ ");
        }

        if (source.getLifeStage() != null && target.getLifeStage() != null) {

            if (source.getLifeStage() == target.getLifeStage()) {
                score += 10;
                reason.append("مرحله زندگی یکسان؛ ");
            } else {
                reason.append("مرحله زندگی متفاوت؛ ");
            }

        } else {
            reason.append("مرحله زندگی نامشخص؛ ");
        }

        score = Math.min(score, 100);

        return new MatchScore(
                BigDecimal.valueOf(score)
                        .setScale(2, java.math.RoundingMode.HALF_UP),
                reason.toString(),
                true
        );
    }

    private double similarity(String first, String second) {
        String a = normalize(first);
        String b = normalize(second);

        if (a.isBlank() || b.isBlank()) {
            return 0;
        }

        if (a.equals(b)) {
            return 1;
        }

        int distance = levenshtein(a, b);
        int maxLength = Math.max(a.length(), b.length());

        return Math.max(0, 1.0 - (double) distance / maxLength);
    }

    private int levenshtein(String first, String second) {
        int[] previous = new int[second.length() + 1];
        int[] current = new int[second.length() + 1];

        for (int j = 0; j <= second.length(); j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= first.length(); i++) {
            current[0] = i;

            for (int j = 1; j <= second.length(); j++) {
                int cost = first.charAt(i - 1) == second.charAt(j - 1)
                        ? 0 : 1;

                current[j] = Math.min(
                        Math.min(current[j - 1] + 1,
                                previous[j] + 1),
                        previous[j - 1] + cost
                );
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[second.length()];
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return Normalizer.normalize(value, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
    }

    public record MatchScore(
            BigDecimal confidenceScore,
            String reason,
            boolean eligible
    ) {
        public static MatchScore rejected(String reason) {
            return new MatchScore(
                    BigDecimal.ZERO.setScale(2),
                    reason,
                    false
            );
        }
    }
}

