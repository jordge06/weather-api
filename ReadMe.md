# 🌤 Weather Service

A Spring Boot application that provides current weather data for Melbourne (default) or a user-specified city.  
The service uses **Weatherstack** as the primary provider and **OpenWeatherMap** as the failover provider.  
Results are cached for 3 seconds to minimize API calls.

---

## 📦 Features
- Fetches **temperature (°C)** and **wind speed**.
- Default city: **Melbourne**, but supports other cities via `?city=` query param.
- **Failover support**: Uses OpenWeatherMap if Weatherstack is down.
- 3-second **server-side caching**.
- Snake_case JSON responses.
- Unit and controller tests included.

---

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot** (Spring Web / REST)
- **Lombok** (for boilerplate code reduction)
- **Jackson** (JSON serialization)
- **Mockito / JUnit 5** (unit & controller tests)
- **Maven** (build tool)

---

## 🔑 API Keys
You need API keys from:
1. **Weatherstack** → [Get a free API key](https://weatherstack.com/documentation)
2. **OpenWeatherMap** → [Get a free API key](https://openweathermap.org/current)

---

## ⚙️ Configuration
Add your API keys in `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

weather:
  api:
    weatherstack:
      key: YOUR_WEATHERSTACK_API_KEY
    openweathermap:
      key: YOUR_OPENWEATHERMAP_API_KEY
