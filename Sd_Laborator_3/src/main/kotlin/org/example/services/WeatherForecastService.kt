package org.example.services

import org.example.interfaces.WeatherForecastInterface
import org.example.pojo.WeatherForecastData
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.net.URL
import kotlin.math.roundToInt
@Service
class WeatherForecastService (private val timeService: TimeService): WeatherForecastInterface {
    override fun getForecastData(locationId: Int): WeatherForecastData {
        // ID-ul locaţiei nu trebuie codificat, deoarece este numeric
        val forecastDataURL = URL("http://api.weatherapi.com/v1/forecast.json?key=35d24d2748e94bf7bc2215702241003&q=id:$locationId&days=1&aqi=no&alerts=no")

        // preluare conţinut răspuns HTTP la o cerere GET către URL-ul de mai sus
        val rawResponse: String = forecastDataURL.readText()

        // parsare obiect JSON primit
        val responseRootObject = JSONObject(rawResponse)
        val locationObject = responseRootObject.getJSONObject("location")
        val currentWeatherObject = responseRootObject.getJSONObject("current")
        val conditionObject = currentWeatherObject.getJSONObject("condition")
        val forecastObject = responseRootObject.getJSONObject("forecast")
        val forecastDayObject = forecastObject.getJSONArray("forecastday").getJSONObject(0)
        val dayObject = forecastDayObject.getJSONObject("day")

        // construire şi returnare obiect POJO care încapsulează datele meteo
        return WeatherForecastData(
            location = "${locationObject.getString("name")}, ${locationObject.getString("region")}, ${locationObject.getString("country")}",
            date = timeService.getCurrentTime(),
            weatherState = conditionObject.getString("text"),
            weatherStateIconURL = "http:${conditionObject.getString("icon")}",
            windDirection = currentWeatherObject.getString("wind_dir"),
            windSpeed = currentWeatherObject.getDouble("wind_kph").roundToInt(),
            minTemp = dayObject.getDouble("mintemp_c").roundToInt(),
            maxTemp = dayObject.getDouble("maxtemp_c").roundToInt(),
            currentTemp = currentWeatherObject.getDouble("temp_c").roundToInt(),
            humidity = currentWeatherObject.getInt("humidity")
        )
    }
}