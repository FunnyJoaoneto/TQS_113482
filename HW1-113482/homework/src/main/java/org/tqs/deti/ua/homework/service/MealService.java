package org.tqs.deti.ua.homework.service;

import org.springframework.stereotype.Service;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.repository.MealRepository;
import org.tqs.deti.ua.homework.repository.RestaurantRepository;

import java.util.List;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public MealService(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Meal> getMealsForRestaurant(Long restaurantId) {
        return mealRepository.findByRestaurantId(restaurantId);
    }

    public Meal getMealById(Long mealId) {
        return mealRepository.findById(mealId).orElseThrow(() -> new RuntimeException("Meal not found"));
    }

    public Meal createMeal(Long restaurantId, String name, Double price) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant with id " + restaurantId + " does not exist.");
        }
        Meal meal = new Meal();
        meal.setRestaurantId(restaurantId.intValue());
        meal.setName(name);
        meal.setPrice(price);
        return mealRepository.save(meal);
    }

    public Meal updateMeal(Long mealId, String name, Double price) {
        if (!mealRepository.existsById(mealId)) {
            throw new IllegalArgumentException("Meal with id " + mealId + " does not exist.");
        }
        Meal meal = getMealById(mealId);
        meal.setName(name);
        meal.setPrice(price);
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long mealId) {
        if (!mealRepository.existsById(mealId)) {
            throw new IllegalArgumentException("Meal with id " + mealId + " does not exist.");
        }
        Meal meal = getMealById(mealId);
        mealRepository.delete(meal);
    }
}
