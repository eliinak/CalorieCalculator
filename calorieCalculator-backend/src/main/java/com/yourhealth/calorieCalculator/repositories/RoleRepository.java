package com.yourhealth.calorieCalculator.repositories;

import com.yourhealth.calorieCalculator.entities.ERole;
import com.yourhealth.calorieCalculator.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
