package com.example.googlemaps.model.network

import com.example.googlemaps.model.response.WeatherResponse
import com.example.googlemaps.model.response.current.CurrenrWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String
    ):Response<WeatherResponse>

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String
    ):Response<CurrenrWeatherResponse>

}