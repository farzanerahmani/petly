package com.petly.food.repository;

import com.petly.food.entity.PetShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetShopRepository extends JpaRepository<PetShop, Long> {

    List<PetShop> findAllByCityAndActiveTrueOrderByNameAsc(String city);

    List<PetShop> findAllByActiveTrueOrderByNameAsc();
}
