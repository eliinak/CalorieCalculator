package com.yourhealth.calorieCalculator.service;

import com.yourhealth.calorieCalculator.entities.Gender;
import com.yourhealth.calorieCalculator.entities.Lifestyle;
import com.yourhealth.calorieCalculator.entities.User;
import com.yourhealth.calorieCalculator.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) {
        return userRepository.findUsersById(userId);
    }

    public double calculateRecommendedCalories(User user) {
        double recommendedCalories;

        final double ACTIVE_MULTIPLIER = 1.55;
        final double PASSIVE_MULTIPLIER = 1.2;
        final int BASE_CALORIES = 2000;

        double lifestyleMultiplier;
        String lifestyle = user.getLifestyle().getLifestyle().toLowerCase();
        if (lifestyle.equals("active")) {
            lifestyleMultiplier = ACTIVE_MULTIPLIER;
        } else {
            lifestyleMultiplier = PASSIVE_MULTIPLIER;
        }


        recommendedCalories = BASE_CALORIES +
                (13.75 * user.getKilograms()) +
                (5 * user.getHeight()) -
                (6.75 * user.getAge());

        recommendedCalories *= lifestyleMultiplier;

        return recommendedCalories;
    }
}
