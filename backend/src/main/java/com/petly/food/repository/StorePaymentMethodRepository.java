package com.petly.food.repository;

import com.petly.food.entity.StorePaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorePaymentMethodRepository extends JpaRepository<StorePaymentMethod, Long> {

    List<StorePaymentMethod> findAllByPetShopIdAndActiveTrueAndPaymentProviderActiveTrue(Long petShopId);

    List<StorePaymentMethod> findAllByPetShop_IdInAndActiveTrueAndPaymentProvider_ActiveTrueAndVerifiedAtIsNotNull(List<Long> shopIds);
}

