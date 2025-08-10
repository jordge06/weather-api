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
    weather_stack:
      key: 'YOUR_API_KEY'
      weather_url: 'http://api.weatherstack.com/current'
    open_weather_map:
      key: 'YOUR_API_KEY'
      city_url: 'http://api.openweathermap.org/geo/1.0/direct'
      weather_url: 'https://api.openweathermap.org/data/2.5/weather'
```

---

## ▶ How to Build and Run

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/weather-service.git
cd weather-service
```

### 2. Configure API keys
Edit `src/main/resources/application.yml` and set your API keys for Weatherstack and OpenWeatherMap.

### 3. Build the project
```bash
mvn clean install
```
This will:
- Compile the Java code
- Download dependencies
- Run unit tests
- Package the application into a `.jar` file in the `target/` folder

### 4. Run the application
Run with Maven:
```bash
mvn spring-boot:run
```
Or run the packaged `.jar`:
```bash
java -jar target/weather-service-0.0.1-SNAPSHOT.jar
```

The application will be available at:
```
http://localhost:8080
```

---

## 🌐 How to Call the API

**Default city (Melbourne):**
```bash
curl "http://localhost:8080/v1/weather"
```

**Custom city:**
```bash
curl "http://localhost:8080/v1/weather?city=Sydney"
```

**Sample response:**
```json
{
  "wind_speed": 20,
  "temperature_degrees": 29
}
```

---

## 🧪 How to Run Tests
Run all unit and controller tests:
```bash
mvn test
```

Tests cover:
- **Service layer**: failover and caching behavior.
- **Controller layer**: default city handling and JSON format.
- **DTO serialization**: snake_case output verification.

---

## 📂 Project Structure
```
src/
 ├── main/
 │   ├── java/com/example/weather/
 │   │   ├── controller/       # REST controllers
 │   │   ├── service/          # Business logic & failover
 │   │   ├── provider/         # Weather API integrations
 │   │   ├── model/            # DTOs & response objects
 │   │   ├── exception/        # Custom exceptions
 │   └── resources/            # Config files (application.yml)
 └── test/                     # Unit and controller tests
```

---

## 📌 Notes
- Weather results are cached for **3 seconds** to reduce API usage.
- If both providers fail, the service will return cached data if available.
- Ensure Lombok plugin is installed in IntelliJ and **annotation processing** is enabled.

---

## 📜 License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
