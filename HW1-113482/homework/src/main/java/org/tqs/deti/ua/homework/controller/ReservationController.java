package org.tqs.deti.ua.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.service.MealService;
import org.tqs.deti.ua.homework.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final MealService mealService;

    public ReservationController(ReservationService reservationService, MealService mealService) {
        this.reservationService = reservationService;
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestParam List<Long> mealIds,
            @RequestParam LocalDateTime dateTime,
            @RequestParam Integer people) {
        try {
            List<Meal> meals = mealIds.stream()
                    .map(mealService::getMealById)
                    .toList();

            Reservation reservation = reservationService.createReservation(meals, dateTime, people);

            if (reservation == null) {
                return ResponseEntity.badRequest()
                        .body("Invalid reservation: exceeds capacity or mismatch in people vs meals.");
            }

            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getReservation(@PathVariable String code) {
        try {
            Reservation reservation = reservationService.getReservationByCode(code);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/{code}/check-in")
    public ResponseEntity<String> checkIn(@PathVariable String code) {
        try {
            boolean success = reservationService.checkInReservation(code);
            return success
                    ? ResponseEntity.ok("Check-in successful")
                    : ResponseEntity.badRequest().body("Invalid or already used reservation");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{code}/update")
    public ResponseEntity<?> updateReservation(@PathVariable String code,
            @RequestParam List<Long> mealIds,
            @RequestParam LocalDateTime dateTime,
            @RequestParam Integer people) {
        try {
            List<Meal> meals = mealIds.stream()
                    .map(mealService::getMealById)
                    .toList();

            Reservation reservation = reservationService.updateReservation(code, meals, dateTime, people);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteReservation(@PathVariable String code) {
        try {
            boolean success = reservationService.deleteByCode(code);
            return success
                    ? ResponseEntity.ok("Reservation deleted")
                    : ResponseEntity.badRequest().body("Invalid reservation code");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getReservationsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByRestaurant(restaurantId);
            return ResponseEntity.ok(reservations);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
