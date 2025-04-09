package org.tqs.deti.ua.homework.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.tqs.deti.ua.homework.cache.InMemoryCache;
import org.tqs.deti.ua.homework.entities.ForecastDay;
import org.tqs.deti.ua.homework.service.WeatherService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private InMemoryCache<Integer, String> weatherCache;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService();
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(weatherService, "weatherCache", weatherCache);
    }

    @Test
    void whenCacheHit_thenReturnParsedForecast() throws Exception {
        int locationCode = 1010500;
        String mockJson = """
                {
                    "data": [
                        {
                            "forecastDate": "2025-04-08",
                            "tMin": "12.0",
                            "tMax": "20.5",
                            "precipitaProb": "30.0",
                            "predWindDir": "N"
                        },
                        {
                            "forecastDate": "2025-04-09",
                            "tMin": "13.0",
                            "tMax": "21.0",
                            "precipitaProb": "10.0",
                            "predWindDir": "NE"
                        }
                    ]
                }
                """;

        when(weatherCache.get(locationCode)).thenReturn(mockJson);

        List<ForecastDay> result = weatherService.getWeatherForecast(locationCode);

        assertEquals(2, result.size());
        assertEquals("2025-04-08", result.get(0).getForecastDate());
        assertEquals("12.0", result.get(0).getTMin());
        assertEquals("20.5", result.get(0).getTMax());

        verify(weatherCache, times(1)).get(locationCode);
        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void whenCacheMiss_thenCallApiAndCacheResponse() throws Exception {
        int locationCode = 1010500;
        String url = "https://api.ipma.pt/open-data/forecast/meteorology/cities/daily/1010500.json";

        String mockJson = """
                {
                    "data": [
                        {
                            "forecastDate": "2025-04-08",
                            "tMin": "11.0",
                            "tMax": "19.0",
                            "precipitaProb": "50.0",
                            "predWindDir": "SW"
                        }
                    ]
                }
                """;

        when(weatherCache.get(locationCode)).thenReturn(null);
        when(restTemplate.getForObject(url, String.class)).thenReturn(mockJson);

        List<ForecastDay> result = weatherService.getWeatherForecast(locationCode);

        assertEquals(1, result.size());
        assertEquals("2025-04-08", result.get(0).getForecastDate());
        verify(weatherCache).put(locationCode, mockJson, 86400);
    }

    @Test
    void whenJsonInvalid_thenReturnEmptyList() {
        int locationCode = 1010500;
        when(weatherCache.get(locationCode)).thenReturn("INVALID_JSON");

        List<ForecastDay> result = weatherService.getWeatherForecast(locationCode);

        assertTrue(result.isEmpty());
    }
}
