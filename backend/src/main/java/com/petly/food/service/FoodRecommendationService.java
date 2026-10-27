package com.petly.food.service;

import com.petly.auth.service.CurrentUserService;
import com.petly.food.controller.dto.FoodProductResponseDto;
import com.petly.food.controller.dto.FoodRecommendationResponseDto;
import com.petly.food.entity.StorePaymentMethod;
import com.petly.food.entity.enums.FoodLifeStage;
import com.petly.food.repository.FoodProductRepository;
import com.petly.food.repository.StorePaymentMethodRepository;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.enums.Species;
import com.petly.pet.repository.PetBodyConditionRecordRepository;
import com.petly.pet.repository.PetMembershipRepository;
import com.petly.pet.repository.PetRepository;
import com.petly.pet.repository.PetWeightRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */


@Service
@RequiredArgsConstructor
public class FoodRecommendationService {

    private final CurrentUserService currentUserService;
    private final PetRepository petRepository;
    private final PetMembershipRepository petMembershipRepository;
    private final PetWeightRecordRepository petWeightRecordRepository;
    private final PetBodyConditionRecordRepository petBodyConditionRecordRepository;
    private final FoodProductRepository foodProductRepository;
    private final FoodRankingService foodRankingService;

    private final StorePaymentMethodRepository storePaymentMethodRepository;

    @Transactional(readOnly = true)
    public FoodRecommendationResponseDto getRecommendations(Long petId) {
        Long userId = currentUserService.getCurrentUserId();

        boolean hasAccess =
                petMembershipRepository.existsByUserIdAndPetId(userId, petId);

        if (!hasAccess) {
            throw new AccessDeniedException(
                    "You do not have access to this pet"
            );
        }

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pet not found")
                );

        var weights =
                petWeightRecordRepository
                        .findByPetIdOrderByRecordedDateDesc(petId);

        var bodyConditions =
                petBodyConditionRecordRepository
                        .findByPetIdOrderByRecordedDateDesc(petId);

        var latestWeight = weights.stream()
                .filter(record -> record.getRecordedDate() != null)
                .findFirst();

        var latestBodyCondition = bodyConditions.stream()
                .filter(record -> record.getRecordedDate() != null)
                .findFirst();

        FoodLifeStage expectedLifeStage = determineLifeStage(pet);

        var products =
                foodProductRepository
                        .findAllBySpeciesAndActiveTrueOrderByNameAsc(
                                pet.getSpecies()
                        );

        FoodRecommendationResponseDto response =
                new FoodRecommendationResponseDto();

        response.setPetId(pet.getId());
        response.setPetName(pet.getName());
        response.setSpecies(pet.getSpecies());
        response.setExpectedLifeStage(expectedLifeStage);

        latestWeight.ifPresent(record -> {
            response.setLatestWeightKg(record.getWeightKg());
            response.setWeightRecordedDate(record.getRecordedDate());
        });

        latestBodyCondition.ifPresent(record -> {
            response.setBodyConditionScore(record.getScore());
            response.setBodyConditionRecordedDate(record.getRecordedDate());
        });


        var recommendations = foodRankingService.rank(
                pet,
                expectedLifeStage,
                products
        );

        List<Long> shopIds = recommendations.stream()
                .flatMap(item -> item.getFood().getPrices().stream())
                .map(FoodProductResponseDto.FoodPriceDto::getPetShopId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, List<StorePaymentMethod>> methodsByShop = shopIds.isEmpty()
                ? Map.of()
                : storePaymentMethodRepository
                .findAllByPetShop_IdInAndActiveTrueAndPaymentProvider_ActiveTrueAndVerifiedAtIsNotNull(shopIds)
                .stream()
                .collect(Collectors.groupingBy(
                        method -> method.getPetShop().getId()
                ));

        for (var item : recommendations) {
            for (var price : item.getFood().getPrices()) {
                List<StorePaymentMethod> methods = price.getPetShopId() == null
                        ? List.of()
                        : methodsByShop.getOrDefault(price.getPetShopId(), List.of());

                price.setPaymentMethods(
                        methods.stream()
                                .map(FoodProductResponseDto.PaymentMethodDto::fromEntity)
                                .toList()
                );
            }
        }

        response.setRecommendations(recommendations);

        response.setNotice(
                "این فهرست برای مقایسه اولیه محصولات است، نه تجویز رژیم غذایی. "
                        + "امتیازها تشخیص کیفیت یا مناسب‌بودن پزشکی غذا نیستند. "
                        + "مقدار مصرف روزانه باید با توجه به شرایط پت و راهنمای محصول "
                        + "و در صورت نیاز با نظر دامپزشک تعیین شود."
        );

        return response;
    }

    private FoodLifeStage determineLifeStage(Pet pet) {
        LocalDate birthDate = pet.getBirthDate();

        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            return null;
        }

        Period age = Period.between(birthDate, LocalDate.now());

        if (pet.getSpecies() == Species.DOG) {
            if (age.toTotalMonths() < 12) {
                return FoodLifeStage.PUPPY;
            }

            if (age.getYears() >= 7) {
                return FoodLifeStage.SENIOR;
            }

            return FoodLifeStage.ADULT;
        }

        if (pet.getSpecies() == Species.CAT) {
            if (age.toTotalMonths() < 12) {
                return FoodLifeStage.KITTEN;
            }

            if (age.getYears() >= 7) {
                return FoodLifeStage.SENIOR;
            }

            return FoodLifeStage.ADULT;
        }

        return null;
    }
}
