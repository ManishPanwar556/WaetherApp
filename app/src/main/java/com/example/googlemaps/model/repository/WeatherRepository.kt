package com.example.googlemaps.model.repository

import com.example.googlemaps.model.network.WeatherApiService
import com.example.googlemaps.utils.ConstantUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val weatherApi = Retrofit.Builder().baseUrl(ConstantUtils.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(WeatherApiService::class.java)


    suspend fun getWeatherData(lat: Double, long: Double) =
        weatherApi.getHourlyForecast(lat = lat, long = long, appid = ConstantUtils.API_KEY)


    suspend fun getCurrentWeatherData(lat: Double, long: Double) =
        weatherApi.getCurrentWeather(lat = lat, long = long,appid= ConstantUtils.API_KEY)
}