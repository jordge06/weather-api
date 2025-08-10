package com.example.weather.service;

import com.example.weather.model.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Log4j2
public class WeatherStackProvider implements WeatherProvider {

    @Value("${weather.api.weather_stack.key}")
    private String apiKey;

    @Value("${weather.api.weather_stack.weather_url}")
    private String weatherUrl;

    @Autowired
    private final RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String weatherStackApiUrl = UriComponentsBuilder.fromUriString(weatherUrl)
                .queryParam("access_key", apiKey)
                .queryParam("query", city)
                .toUriString();

        log.info("Weather API URL: {}", weatherStackApiUrl);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(weatherStackApiUrl, JsonNode.class);
        JsonNode body = response.getBody();

        if (body == null || !body.has("current")) {
            throw new RuntimeException("Weatherstack API returned invalid response for city: " + city);
        }

        JsonNode current = body.get("current");

        if (!current.has("wind_speed") || !current.has("temperature")) {
            throw new RuntimeException("Weatherstack API returned incomplete data for city: " + city);
        }

        return new WeatherResponse(
                current.get("wind_speed").asInt(),
                current.get("temperature").asInt()
        );
    }
}
