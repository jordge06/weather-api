package com.example.weather.service;

import com.example.weather.exception.WeatherServiceException;
import com.example.weather.model.WeatherResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final List<WeatherProvider> providers;
    private final WeatherCacheService cacheService;

    public WeatherService(List<WeatherProvider> providers, WeatherCacheService cacheService) {
        this.providers = providers;
        this.cacheService = cacheService;
    }

    public WeatherResponse getWeather(String city) {
        WeatherResponse cached = cacheService.get(city);
        if (cached != null) return cached;

        for (WeatherProvider provider : providers) {
            try {
                WeatherResponse response = provider.getWeather(city);
                cacheService.put(city, response);
                return response;
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }

        if (cached != null) return cached; // fallback to previous cache if available

        throw new WeatherServiceException("All weather providers failed");
    }
}
