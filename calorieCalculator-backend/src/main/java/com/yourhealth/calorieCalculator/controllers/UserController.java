package com.yourhealth.calorieCalculator.controllers;

import com.yourhealth.calorieCalculator.entities.*;
import com.yourhealth.calorieCalculator.repositories.*;
import com.yourhealth.calorieCalculator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    LifestyleRepository lifestyleRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> getAllPersons() {
        return userRepository.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdate(@RequestParam(required = false) Long id,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String surname,
                                          @RequestParam(required = false) Integer age,
                                          @RequestParam(required = false) String gender,
                                          @RequestParam(required = false) Integer kilograms,
                                          @RequestParam(required = false) Integer height,
                                          @RequestParam(required = false) String lifestyle,
                                          @RequestParam(required = false) String email

                                          ) {

        User user = userRepository.findUsersById(id);

        if (name != null) {
            user.setName(name);
        }

        if (surname != null) {
            user.setSurname(surname);
        }

        if (age != null) {
            user.setAge(age);
        }

        if (gender != null) {
            user.setGender(genderRepository.findGenderByName(gender.toLowerCase()));
        }

        if (kilograms != null) {
            user.setKilograms(kilograms);
        }

        if (height != null) {
            user.setHeight(height);
        }

        if (lifestyle != null) {
            user.setLifestyle(lifestyleRepository.findLifestyleType(lifestyle.toLowerCase()));
        }

        if (email != null) {
            user.setEmail(email);
        }

        user = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("message", "Successfully saved!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/saveNeedKcal")
    public ResponseEntity<?> saveOrUpdateKcal(@RequestBody Map<String, Object> request) {
        Long id = Long.parseLong(request.get("id").toString());
        Integer needKcal = Integer.parseInt(request.get("needKcal").toString());

        User user = userRepository.findUsersById(id);
        if (user == null) {
        }

        user.setNeedKcal(needKcal);
        user = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("message", "Successfully saved kcal!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/food/{id}")
    public ResponseEntity<?> getFood(@PathVariable Long id) {
        Food food = foodRepository.findFoodById(id);
        if (food != null) {
            return ResponseEntity.ok(food);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value="/user")
    public Optional<User> getUsers(@RequestParam(required = false)  Long id) {
        Long currentUserId = (id == null) ? 2 : id;
        return userRepository.findById(currentUserId);
    }

    @GetMapping("/search/id")
    public Optional<User> getUserById(@RequestParam(required = false)  Long id){
        return userRepository.findById(id==null? 2:id);
    }


    @PostMapping(value = "/user/calculate_need_kcal/{userId}")
    public ResponseEntity<?> calculateUserNeedKcal(@PathVariable Long userId, @RequestBody User userData) {
        User user = userService.getUser(userId);

        if (user != null) {
            user.setGender(userData.getGender());
            user.setHeight(userData.getHeight());
            user.setKilograms(userData.getKilograms());
            user.setLifestyle(userData.getLifestyle());

            double needKcal = userService.calculateRecommendedCalories(user);
            return ResponseEntity.ok(needKcal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-food-options")
    public ResponseEntity<Food> addNewFoodOption(@RequestBody Food newFood) {
        try {
            Food savedFood = foodRepository.save(newFood);
            return ResponseEntity.ok(savedFood);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/food-options")
    public ResponseEntity<List<Food>> getFoodOptions() {
        try {
            List<Food> foodOptions = foodRepository.findAll();
            return ResponseEntity.ok(foodOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/checkEmailExists")
    public boolean checkEmailExists(String email) {
        List<User> usersWithEmail = userRepository.findByEmail(email);
        return !usersWithEmail.isEmpty();
    }
}
