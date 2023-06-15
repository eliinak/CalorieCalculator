package com.yourhealth.calorieCalculator.repositories;

import com.yourhealth.calorieCalculator.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findFoodById(Long id);
    List<Food> findByFood(String food);}
