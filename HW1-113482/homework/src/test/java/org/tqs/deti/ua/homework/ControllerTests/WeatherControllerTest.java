package org.tqs.deti.ua.homework.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.tqs.deti.ua.homework.controller.WeatherController;
import org.tqs.deti.ua.homework.entities.ForecastDay;
import org.tqs.deti.ua.homework.service.WeatherService;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeatherForecast() throws Exception {
        ForecastDay forecastDay = new ForecastDay();
        forecastDay.setForecastDate("2025-04-08");
        forecastDay.setTMin("15");
        forecastDay.setTMax("20");
        forecastDay.setPrecipitaProb("10");
        forecastDay.setPredWindDir("N");

        // Mock the service to return a list of ForecastDay
        when(weatherService.getWeatherForecast(1010500)).thenReturn(List.of(forecastDay));

        mockMvc.perform(get("/weather/forecast/1010500"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"forecastDate\":\"2025-04-08\",\"precipitaProb\":\"10\",\"predWindDir\":\"N\",\"tmin\":\"15\",\"tmax\":\"20\"}]")); // Assert
                                                                                                                                                // JSON
                                                                                                                                                // response
    }
}
