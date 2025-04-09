package org.tqs.deti.ua.homework.service;

import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.entities.Restaurant;
import org.tqs.deti.ua.homework.repository.MealRepository;
import org.tqs.deti.ua.homework.repository.ReservationRepository;
import org.tqs.deti.ua.homework.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    private final ReservationRepository reservationRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, ReservationRepository reservationRepository,
            MealRepository mealRepository,
            MealService mealService) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist.");
        }
        return restaurantRepository.findById(id).orElseThrow();
    }

    public List<Meal> getMealsByRestaurantId(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist.");
        }
        return mealRepository.findByRestaurantId(id);
    }

    public List<Reservation> getReservationsByRestaurantId(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist.");
        }
        return reservationRepository.findByRestaurantId(id);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist.");
        }
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setName(updatedRestaurant.getName());
        restaurant.setCapacity(updatedRestaurant.getCapacity());
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist.");
        }
        Restaurant restaurant = getRestaurantById(id);
        mealRepository.deleteAllByRestaurantId(id);
        restaurantRepository.delete(restaurant);
    }
}
