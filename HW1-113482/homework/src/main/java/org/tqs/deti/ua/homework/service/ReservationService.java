package org.tqs.deti.ua.homework.service;

import org.springframework.stereotype.Service;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.MealReservation;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.repository.ReservationRepository;
import org.tqs.deti.ua.homework.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(List<Meal> meals, LocalDateTime dateTime, Integer people) {
        if (meals.isEmpty())
            return null;

        Integer restaurantId = meals.get(0).getRestaurantId(); // Assuming all meals from same restaurant

        int alreadyReserved = reservationRepository
                .findByRestaurantIdAndDateTime(restaurantId.longValue(), dateTime)
                .stream()
                .mapToInt(Reservation::getPeople)
                .sum();

        int restaurantCapacity = restaurantRepository.findById(restaurantId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"))
                .getCapacity();

        if (alreadyReserved + people > restaurantCapacity) {
            return null; // Exceeds capacity
        }

        String code = generateReservationCode();
        Reservation reservation = new Reservation(code, restaurantId, dateTime, people);

        if (meals.size() != people) {
            return null; // Number of meals must match number of people
        }

        for (Meal meal : meals) {
            Optional<MealReservation> existing = reservation.getMealReservations()
                    .stream()
                    .filter(mr -> mr.getMeal().getId().equals(meal.getId()))
                    .findFirst();

            if (existing.isPresent()) {
                // If it already exists, increase the quantity
                MealReservation mealReservation = existing.get();
                mealReservation.setQuantity(mealReservation.getQuantity() + 1);
            } else {
                // Otherwise, create a new one
                MealReservation mealReservation = new MealReservation();
                mealReservation.setMeal(meal);
                mealReservation.setReservation(reservation);
                mealReservation.setQuantity(1);
                reservation.getMealReservations().add(mealReservation);
            }
        }

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationByCode(String code) {
        if (!reservationRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Reservation with code " + code + " does not exist.");
        }
        return reservationRepository.findByCode(code);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public boolean checkInReservation(String code) {
        if (!reservationRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Reservation with code " + code + " does not exist.");
        }
        Reservation reservation = reservationRepository.findByCode(code);
        if (reservation != null && !reservation.isClosed()) {
            reservation.setClosed(true);
            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }

    public boolean deleteByCode(String code) {
        if (!reservationRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Reservation with code " + code + " does not exist.");
        }
        Reservation reservation = reservationRepository.findByCode(code);
        if (reservation != null) {
            reservationRepository.delete(reservation);
            return true;
        }
        return false;
    }

    private String generateReservationCode() {
        String code;
        do {
            long timestamp = System.currentTimeMillis(); // Milliseconds since epoch
            String randomPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            code = "RES-" + timestamp + randomPart;
        } while (reservationRepository.existsByCode(code));
        return code;
    }

    public List<Reservation> getReservationsByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant with id " + restaurantId + " does not exist.");
        }
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    public Reservation updateReservation(String code, List<Meal> meals, LocalDateTime dateTime, Integer people) {
        if (!reservationRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Reservation with code " + code + " does not exist.");
        }
        Reservation reservation = getReservationByCode(code);

        reservation.setDateTime(dateTime);
        reservation.setPeople(people);

        // Clear existing meal reservations
        reservation.getMealReservations().clear();

        for (Meal meal : meals) {
            MealReservation mealReservation = new MealReservation();
            mealReservation.setMeal(meal);
            mealReservation.setReservation(reservation);
            mealReservation.setQuantity(1);
            reservation.getMealReservations().add(mealReservation);
        }

        return reservationRepository.save(reservation);
    }

}
