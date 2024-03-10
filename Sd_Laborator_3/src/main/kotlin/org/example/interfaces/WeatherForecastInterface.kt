package org.example.interfaces

import org.example.pojo.WeatherForecastData

interface WeatherForecastInterface {
    fun getForecastData(locationId: Int): WeatherForecastData
}