package org.tqs.deti.ua.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tqs.deti.ua.homework.entities.Meal;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByRestaurantId(Long restaurantId);

    void deleteAllByRestaurantId(Long id);
}