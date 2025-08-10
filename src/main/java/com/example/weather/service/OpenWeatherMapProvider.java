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
public class OpenWeatherMapProvider implements WeatherProvider {

    @Value("${weather.api.open_weather_map.key}")
    private String apiKey;

    @Value("${weather.api.open_weather_map.weather_url}")
    private String weatherUrl;

    @Value("${weather.api.open_weather_map.weather_url}")
    private String cityUrl;

    @Autowired
    private final RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        // Build geo API URL
        String cityQuery = city + ",VIC,AU";
        String geoApiUrl = UriComponentsBuilder.fromUriString(cityUrl)
                .queryParam("q", cityQuery)
                .queryParam("limit", 1)
                .queryParam("appid", apiKey)
                .toUriString();

        log.info("City API URL: {}", geoApiUrl);

        ResponseEntity<JsonNode> geoResponse = restTemplate.getForEntity(geoApiUrl, JsonNode.class);
        JsonNode geoBody = geoResponse.getBody();

        if (geoBody == null || !geoBody.isArray() || geoBody.isEmpty()) {
            throw new RuntimeException("Geo API returned no results for city: " + city);
        }

        JsonNode firstResult = geoBody.get(0);
        String lat = firstResult.get("lat").asText();
        String lon = firstResult.get("lon").asText();

        // Build weather API URL
        String weatherApiUrl = UriComponentsBuilder.fromUriString(weatherUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .toUriString();

        log.info("Weather API URL: {}", weatherApiUrl);

        ResponseEntity<JsonNode> weatherResponse = restTemplate.getForEntity(weatherApiUrl, JsonNode.class);
        JsonNode weatherBody = weatherResponse.getBody();

        if (weatherBody == null || !weatherBody.has("wind") || !weatherBody.has("main")) {
            throw new RuntimeException("Weather API returned incomplete data for coordinates: " + lat + "," + lon);
        }

        return new WeatherResponse(
                weatherBody.get("wind").get("speed").asInt(),
                weatherBody.get("main").get("temp").asInt()
        );
    }
}
