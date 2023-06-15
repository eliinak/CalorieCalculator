package com.yourhealth.calorieCalculator.repositories;

import com.yourhealth.calorieCalculator.entities.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenderRepository extends JpaRepository<Gender, Long> {
    @Query("select g FROM Gender g WHERE lower(g.genderType) = :gender")
    Gender findGenderByName(String gender);
}
