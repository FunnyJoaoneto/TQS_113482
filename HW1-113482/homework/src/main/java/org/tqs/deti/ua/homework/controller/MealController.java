package org.tqs.deti.ua.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.service.MealService;

import java.util.List;

@RestController
@RequestMapping("/meals")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getMeals(@RequestParam Long restaurantId) {
        return ResponseEntity.ok(mealService.getMealsForRestaurant(restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mealService.getMealById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createMeal(
            @RequestParam Long restaurantId,
            @RequestParam String name,
            @RequestParam Double price) {
        try {
            return ResponseEntity.ok(mealService.createMeal(restaurantId, name, price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMeal(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Double price) {
        try {
            return ResponseEntity.ok(mealService.updateMeal(id, name, price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        try {
            mealService.deleteMeal(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
