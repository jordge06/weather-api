package com.example.weather.service;

import com.example.weather.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherProvider primaryProvider;

    @Mock
    private WeatherProvider failoverProvider;

    @Mock
    private WeatherCacheService cacheService;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Manually inject the mocked providers list
        weatherService = new WeatherService(
                List.of(primaryProvider, failoverProvider),
                cacheService
        );
    }

    @Test
    void shouldReturnFromPrimaryProvider() {
        when(cacheService.get("melbourne")).thenReturn(null);
        when(primaryProvider.getWeather("melbourne")).thenReturn(new WeatherResponse(20, 29));

        WeatherResponse result = weatherService.getWeather("melbourne");

        assertEquals(20, result.getWindSpeed());
        assertEquals(29, result.getTemperatureDegrees());
    }
}


