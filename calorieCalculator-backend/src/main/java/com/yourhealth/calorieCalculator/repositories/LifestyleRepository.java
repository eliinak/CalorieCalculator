package com.yourhealth.calorieCalculator.repositories;

import com.yourhealth.calorieCalculator.entities.Gender;
import com.yourhealth.calorieCalculator.entities.Lifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LifestyleRepository extends JpaRepository<Lifestyle, Long> {
    @Query("select l FROM Lifestyle l WHERE lower(l.lifestyleType) = :lifestyle")
    Lifestyle findLifestyleType(String lifestyle);
}
