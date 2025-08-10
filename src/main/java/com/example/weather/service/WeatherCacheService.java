package com.example.weather.service;

import com.example.weather.model.WeatherResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherCacheService {

    private final Map<String, CachedResult> cache = new ConcurrentHashMap<>();

    public WeatherResponse get(String city) {
        CachedResult result = cache.get(city.toLowerCase());
        if (result != null && System.currentTimeMillis() - result.timestamp < 3000) {
            return result.response;
        }
        return null;
    }

    public void put(String city, WeatherResponse response) {
        cache.put(city.toLowerCase(), new CachedResult(response));
    }

    private static class CachedResult {
        WeatherResponse response;
        long timestamp;

        CachedResult(WeatherResponse response) {
            this.response = response;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
