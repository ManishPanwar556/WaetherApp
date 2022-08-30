package com.example.googlemaps.model.response

data class WeatherResponseItem(
    val dt:Long?,
    val main:MainWeatherResponse?,
    val weather:List<AtmosphereResponse>?,
    val wind:WindResponse,
    val visibility:Long,
    val pop:Double?,
    val dt_txt:String?

)
