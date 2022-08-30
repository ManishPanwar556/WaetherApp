package com.example.googlemaps.model.response

data class WeatherResponse(
    val cod:Int?,
    val message:Int?,
    val cnt:Int?,
    val list:List<WeatherResponseItem>?,
    val city:CityResponse?
)
