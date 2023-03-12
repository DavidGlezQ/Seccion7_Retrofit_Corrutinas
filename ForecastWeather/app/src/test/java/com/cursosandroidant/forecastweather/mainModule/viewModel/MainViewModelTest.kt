package com.cursosandroidant.forecastweather.mainModule.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cursosandroidant.forecastweather.MainCoroutineRule
import com.cursosandroidant.forecastweather.common.dataAccess.WeatherForecastService
import com.cursosandroidant.forecastweather.entities.WeatherForecastEntity
import com.cursosandroidant.historicalweatherref.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by davidgonzalez on 12/03/23
 */
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var service: WeatherForecastService

    companion object {
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setup() {
        mainViewModel = MainViewModel()
        service = retrofit.create(WeatherForecastService::class.java)
    }

    //Verificar respuesta corecta
    @Test
    fun checkCurrentWeatherIsNotNullTest() {
       runBlocking {//Al trabajar con corrutinas debemos de poner el codigo dentro de este bloque de codigo
           val result = service.getWeatherForecastByCoordinates(19.4342, -99.1962,
               "a1d8cf4ff0c02cac0b78fd4b66fc1125", "metric", "en")

           assertThat(result.current, `is`(notNullValue()))
       }
    }

    @Test
    fun checkTimezoneReturnsMexicoCityTest() {
        runBlocking {
            val result = service.getWeatherForecastByCoordinates(19.4342, -99.1962,
                "a1d8cf4ff0c02cac0b78fd4b66fc1125", "metric", "en")

            assertThat(result.current, `is`("America/Mexico_City"))
        }
    }

    //Verficar respuesta del servidor
    @Test
    fun checkErrorResponseWithOnlyCoordinatesTest() {
        runBlocking {
            try {
                service.getWeatherForecastByCoordinates(19.4342, -99.1962,
                    "", "", "")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 Unauthorized"))
            }
        }
    }

    @Test
    fun checkHourlySizeTest() {
        runBlocking {
            mainViewModel.getWeatherAndForecast(19.4342, -99.1962,
                "a1d8cf4ff0c02cac0b78fd4b66fc1125", "metric", "en")
            val result = mainViewModel.getResult().getOrAwaitValue()
            assertThat(result.hourly.size, `is`(48))
        }
    }
}