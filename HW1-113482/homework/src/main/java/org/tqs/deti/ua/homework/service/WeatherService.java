package org.tqs.deti.ua.homework.service;

import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import org.tqs.deti.ua.homework.cache.CacheStatistics;
import org.slf4j.Logger;

import org.tqs.deti.ua.homework.cache.InMemoryCache;
import org.tqs.deti.ua.homework.entities.ForecastDay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private static final String API_URL = "https://api.ipma.pt/open-data/forecast/meteorology/cities/daily/";

    private final RestTemplate restTemplate = new RestTemplate();
    private final InMemoryCache<Integer, String> weatherCache = new InMemoryCache<>();

    private final CacheStatistics weatherCacheStatistics = new CacheStatistics();

    public List<ForecastDay> getWeatherForecast(Integer code) {
        List<ForecastDay> list = new ArrayList<>();

        try {
            String cached = weatherCache.get(code);
            String json;
            if (cached != null) {
                log.info("Cache hit for location code: {}", code);
                weatherCacheStatistics.incrementHits();
                json = cached;
            } else {
                log.info("Cache miss for location code: {}", code);
                weatherCacheStatistics.incrementMisses();
                String url = API_URL + code + ".json";
                json = restTemplate.getForObject(url, String.class);
                weatherCache.put(code, json, 86400);
                weatherCacheStatistics.incrementPuts();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode data = root.path("data");

            for (JsonNode node : data) {
                ForecastDay day = new ForecastDay();
                day.setForecastDate(node.path("forecastDate").asText());
                day.setTMin(node.path("tMin").asText());
                day.setTMax(node.path("tMax").asText());
                day.setPrecipitaProb(node.path("precipitaProb").asText());
                day.setPredWindDir(node.path("predWindDir").asText());
                list.add(day);
            }
        } catch (Exception e) {
            log.error("Failed to parse weather forecast: {}", e.getMessage());
        }

        return list;
    }

    public InMemoryCache<Integer, String> getWeatherCache() {
        return weatherCache;
    }

    public CacheStatistics getWeatherCacheStatistics() {
        return weatherCacheStatistics;
    }

}
