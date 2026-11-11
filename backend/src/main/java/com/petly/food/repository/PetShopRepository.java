package com.petly.food.repository;

import com.petly.food.entity.PetShop;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetShopRepository extends JpaRepository<PetShop, Long> {

    List<PetShop> findAllByCityAndActiveTrueOrderByNameAsc(String city);

    List<PetShop> findAllByActiveTrueOrderByNameAsc();

    Optional<PetShop> findByWebsiteUrl(String websiteUrl);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT p
    FROM PetShop p
    WHERE p.id = :petShopId
      AND p.active = true
""")
    Optional<PetShop> findActiveByIdForUpdate(
            @Param("petShopId") Long petShopId
    );

}
