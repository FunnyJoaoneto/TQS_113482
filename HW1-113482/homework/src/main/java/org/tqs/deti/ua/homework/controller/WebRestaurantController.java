package org.tqs.deti.ua.homework.controller;

import org.tqs.deti.ua.homework.entities.ForecastDay;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.entities.Restaurant;
import org.tqs.deti.ua.homework.service.RestaurantService;
import org.tqs.deti.ua.homework.service.WeatherService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebRestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    private WeatherService weatherService;

    public WebRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/web/restaurants")
    public String listRestaurants(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "restaurants"; // → templates/restaurants.html
    }

    @GetMapping("/web/restaurants/{id}")
    public String showRestaurantDetails(@PathVariable("id") Long id, Model model) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("meals", restaurantService.getMealsByRestaurantId(id));

        try {
            Integer locationCode = restaurant.getLocationCode(); // You need this field in your model
            List<ForecastDay> forecastList = weatherService.getWeatherForecast(locationCode);
            model.addAttribute("forecastList", forecastList);

        } catch (Exception e) {
            model.addAttribute("forecastList", null);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime opening = LocalTime.parse(restaurant.getOpeningTime(), timeFormatter);
        LocalTime closing = LocalTime.parse(restaurant.getClosingTime(), timeFormatter);

        // Fetch all existing reservations for this restaurant
        List<Reservation> reservations = restaurantService.getReservationsByRestaurantId(id);
        Map<LocalDateTime, Integer> reservationsBySlot = new HashMap<>();
        for (Reservation reservation : reservations) {
            reservationsBySlot.merge(reservation.getDateTime(), reservation.getPeople(), Integer::sum);
        }

        List<Map<String, Object>> days = new ArrayList<>();
        List<List<Map<String, Object>>> slots = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            LocalDate date = today.plusDays(i);
            days.add(Map.of(
                    "label", date.getDayOfWeek().toString().substring(0, 3) + " " + date.toString(),
                    "date", date));

            int totalSlots = closing.getHour() - opening.getHour();
            for (int h = 0; h < totalSlots; h++) {
                if (slots.size() <= h)
                    slots.add(new ArrayList<>());

                LocalDateTime dateTime = LocalDateTime.of(date, opening.plusHours(h));
                int reserved = reservationsBySlot.getOrDefault(dateTime, 0);
                boolean available = reserved < restaurant.getCapacity();

                slots.get(h).add(Map.of(
                        "datetime", dateTime,
                        "available", available,
                        "reserved", reserved));
            }
        }

        model.addAttribute("days", days);
        model.addAttribute("slots", slots);

        return "restaurant_details";
    }
}
