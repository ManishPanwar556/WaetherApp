package com.example.googlemaps.utils

data class ApiResponse<T>(
    val ResponseCode: Int,
    val Result: T?,
    val errorMessage:String
)
