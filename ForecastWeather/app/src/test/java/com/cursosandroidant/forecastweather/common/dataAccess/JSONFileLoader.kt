package com.cursosandroidant.forecastweather.common.dataAccess

import com.cursosandroidant.forecastweather.entities.WeatherForecastEntity
import com.google.gson.Gson
import java.io.InputStreamReader

/**
 * Created by davidgonzalez on 13/03/23
 */
class JSONFileLoader {
    private var jsonStr: String? = null

    fun loadJSONString(file: String): String? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadWeatherForecastEntity(file: String): WeatherForecastEntity? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, WeatherForecastEntity::class.java)
    }
}