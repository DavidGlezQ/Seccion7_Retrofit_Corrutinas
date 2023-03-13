package com.cursosandroidant.forecastweather.common.dataAccess

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

/**
 * Created by davidgonzalez on 13/03/23
 */
@RunWith(MockitoJUnitRunner::class)
class ResponseServerTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun readJsonFileSuccess() {
        val reader = JSONFileLoader().loadJSONString("weather_forecast_response_success.json")
        assertThat(reader, `is`(notNullValue()))
        assertThat(reader, containsString("America/Mexico_City"))
    }

    @Test
    fun getWeatherForecastTimezoneExist() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(JSONFileLoader().loadJSONString("weather_forecast_response_success.json") ?: "{errorCode:34}")
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("\"timezone\""))
    }
}