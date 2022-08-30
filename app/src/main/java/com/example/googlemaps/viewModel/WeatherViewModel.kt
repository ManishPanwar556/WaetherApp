package com.example.googlemaps.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemaps.model.repository.WeatherRepository
import com.example.googlemaps.model.response.WeatherResponse
import com.example.googlemaps.model.response.current.CurrenrWeatherResponse
import com.example.googlemaps.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG = "VIEWMODEL"

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherData = MutableLiveData<ApiResponse<WeatherResponse?>>()
    val weatherData: LiveData<ApiResponse<WeatherResponse?>> = _weatherData

    private val _currentWeatherData=MutableLiveData<ApiResponse<CurrenrWeatherResponse?>>()
    val currentWeatherData:LiveData<ApiResponse<CurrenrWeatherResponse?>> = _currentWeatherData

    fun getWeatherData(long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getWeatherData(lat = lat, long = long)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _weatherData.postValue(ApiResponse(ResponseCode = 200, response.body(), ""))
                    getCurrentWeatherData(lat, long)
                }
            } else {
                withContext(Dispatchers.Main) {
                    _weatherData.postValue(
                        ApiResponse(
                            ResponseCode = response.code(),
                            null,
                            response.message()
                        )
                    )
                }

            }
        }
    }

    fun getCurrentWeatherData(lat: Double,long: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCurrentWeatherData(lat = lat, long = long)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _currentWeatherData.postValue(ApiResponse(ResponseCode = 200, response.body(), ""))
                }
            } else {
                withContext(Dispatchers.Main) {
                    _currentWeatherData.postValue(
                        ApiResponse(
                            ResponseCode = response.code(),
                            null,
                            response.message()
                        )
                    )
                }

            }
        }
    }

}