package org.tqs.deti.ua.homework.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.tqs.deti.ua.homework.controller.CacheController;
import org.tqs.deti.ua.homework.cache.InMemoryCache;
import org.tqs.deti.ua.homework.cache.CacheStatistics;
import org.tqs.deti.ua.homework.service.WeatherService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CacheController.class)
class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetCache() throws Exception {
        InMemoryCache<Integer, String> mockCache = new InMemoryCache<>();
        mockCache.put(1010500, "Sunny");

        when(weatherService.getWeatherCache()).thenReturn(mockCache);

        mockMvc.perform(get("/cache"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("weatherCache")));
    }

    @Test
    void testGetCacheStatistics() throws Exception {
        CacheStatistics mockStats = new CacheStatistics();
        mockStats.incrementHits();

        when(weatherService.getWeatherCacheStatistics()).thenReturn(mockStats);

        mockMvc.perform(get("/cache/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("weatherCacheStatistics")));
    }
}
