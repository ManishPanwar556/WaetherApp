package com.example.googlemaps.model.response.current

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)