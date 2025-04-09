package org.tqs.deti.ua.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tqs.deti.ua.homework.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast/{locationCode}")
    /**
     * Endpoint to get the weather forecast for a specific location.
     *
     * @param locationCode The location code for which to retrieve the weather
     *                     forecast.
     * @return A JSON response containing the weather forecast.
     */
    public ResponseEntity<String> getWeatherForecast(@PathVariable Integer locationCode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String forecast = objectMapper.writeValueAsString(weatherService.getWeatherForecast(locationCode));
            return ResponseEntity.ok(forecast);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing weather forecast data");
        }
    }
}
