package com.example.weather.controller;

import com.example.weather.model.WeatherResponse;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherForGivenCity() throws Exception {
        WeatherResponse response = new WeatherResponse(20, 29);
        when(weatherService.getWeather("Sydney")).thenReturn(response);

        mockMvc.perform(get("/v1/weather")
                        .param("city", "Sydney"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wind_speed").value(20))
                .andExpect(jsonPath("$.temperature_degrees").value(29));
    }

    @Test
    void shouldDefaultToMelbourneWhenNoCityProvided() throws Exception {
        WeatherResponse response = new WeatherResponse(20, 29);
        when(weatherService.getWeather("Melbourne")).thenReturn(response);

        mockMvc.perform(get("/v1/weather"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wind_speed").value(20))
                .andExpect(jsonPath("$.temperature_degrees").value(29));
    }
}

