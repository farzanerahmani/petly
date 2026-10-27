package com.petly.food.repository;

import com.petly.food.entity.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author farzane.rahmani
 * @created 10/25/2026
 */
import java.util.List;

public interface PaymentProviderRepository
        extends JpaRepository<PaymentProvider, Long> {

    List<PaymentProvider> findAllByActiveTrueOrderByNameAsc();
}

