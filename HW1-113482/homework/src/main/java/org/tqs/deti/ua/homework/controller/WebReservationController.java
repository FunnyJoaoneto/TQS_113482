package org.tqs.deti.ua.homework.controller;

import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.MealReservation;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.entities.Restaurant;
import org.tqs.deti.ua.homework.service.MealService;
import org.tqs.deti.ua.homework.service.ReservationService;
import org.tqs.deti.ua.homework.service.RestaurantService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/reservations")
public class WebReservationController {

    private final ReservationService reservationService;
    private final MealService mealService;
    private final RestaurantService restaurantService;

    public WebReservationController(ReservationService reservationService, MealService mealService,
            RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
        this.mealService = mealService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public String createReservation(@RequestParam("mealIds") List<Long> mealIds,
            @RequestParam Map<String, String> allParams,
            @RequestParam("dateTime") String dateTime,
            @RequestParam("people") int people,
            Model model) {
        LocalDateTime dt = LocalDateTime.parse(dateTime);

        // Extract quantity per meal
        Map<Long, Integer> mealQuantities = new HashMap<>();
        for (Long mealId : mealIds) {
            String key = "quantities[" + mealId + "]";
            if (allParams.containsKey(key)) {
                try {
                    int qty = Integer.parseInt(allParams.get(key));
                    if (qty > 0)
                        mealQuantities.put(mealId, qty);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        // Build meals list based on quantities
        List<Meal> meals = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : mealQuantities.entrySet()) {
            Meal meal = mealService.getMealById(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                meals.add(meal);
            }
        }

        Reservation reservation = reservationService.createReservation(meals, dt, people);

        if (reservation == null) {
            model.addAttribute("errorMessage",
                    "Reservation failed. Failed to create reservation. Please check the restaurant's capacity and meal quantities.");
            return "reservation_failed";
        }

        model.addAttribute("reservationCode", reservation.getCode());
        return "reservation_success";
    }

    @GetMapping("/success")
    public String showReservationSuccess(Model model) {
        return "reservation_success";
    }

    @GetMapping("/search")
    public String findReservation(@RequestParam("code") String code, Model model) {
        Reservation reservation = null;
        try {
            reservation = reservationService.getReservationByCode(code);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Reservation not found.");
            return "reservation_not_found";
        }
        if (reservation == null) {
            model.addAttribute("error", "Reservation not found.");
            return "reservation_not_found";
        }

        model.addAttribute("reservation", reservation);
        List<MealReservation> mealReservations = reservation.getMealReservations();
        model.addAttribute("mealReservations", mealReservations);

        Restaurant res = restaurantService.getRestaurantById(reservation.getRestaurantId().longValue());
        model.addAttribute("restaurant", res);
        return "reservation_detail";
    }

    @PostMapping("/check-in")
    public String checkInReservation(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        boolean success = reservationService.checkInReservation(code);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Reservation checked in successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Check-in failed: Reservation not found or already closed.");
        }
        return "redirect:/web/reservations/search?code=" + code;
    }

    @PostMapping("/delete")
    public String deleteReservation(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        reservationService.deleteByCode(code);
        redirectAttributes.addFlashAttribute("successMessage", "Reservation cancelled.");
        return "redirect:/web/restaurants";
    }

}
