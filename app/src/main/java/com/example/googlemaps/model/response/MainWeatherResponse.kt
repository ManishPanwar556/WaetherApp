package com.example.googlemaps.model.response

data class MainWeatherResponse(
    val temp:Float?,
    val feels_like:Float?,
    val temp_min:Float?,
    val temp_max:Float?,
    val sea_level:Long?,
    val pressure:Long?,
    val grnd_level:Long?,
    val humidity:Int?,
)
