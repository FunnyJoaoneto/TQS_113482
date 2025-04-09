package org.tqs.deti.ua.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.tqs.deti.ua.homework.service.WeatherService;
import org.tqs.deti.ua.homework.cache.InMemoryCache;
import org.tqs.deti.ua.homework.cache.CacheStatistics;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private WeatherService weatherApi;
    private static final Logger log = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    public CacheController(WeatherService weatherApi) {
        this.weatherApi = weatherApi;
    }

    @GetMapping("")
    public String getCache() {
        log.info("GET /cache");
        InMemoryCache<Integer, String> weatherCache = weatherApi.getWeatherCache();

        return "[ weatherCache: " + weatherCache.toString() + " ]";
    }

    @GetMapping("/statistics")
    public String getCacheStatistics() {
        log.info("GET /cache/statistics");
        CacheStatistics weatherCacheStatistics = weatherApi.getWeatherCacheStatistics();

        return "[ weatherCacheStatistics: " + weatherCacheStatistics.toString() + " ]";
    }
}
