package com.example.googlemaps.view

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.googlemaps.adapters.WeatherGraphAdapter
import com.example.googlemaps.adapters.WeatherTimeUpdateAdapter
import com.example.googlemaps.databinding.ActivityMainBinding
import com.example.googlemaps.model.response.*
import com.example.googlemaps.model.response.current.CurrenrWeatherResponse
import com.example.googlemaps.viewModel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val locationRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getLocation()
            } else {
                Toast.makeText(this, "Please Grant Permission for location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    private var precipProb:String=""
    private lateinit var graphAdapter:WeatherGraphAdapter
    private lateinit var adapter: WeatherTimeUpdateAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            locationRequestLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        initObservers()
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                Log.d("LOCATION", "${it.latitude} ${it.longitude}")
                viewModel.getWeatherData(it.longitude, it.latitude)
            }.addOnFailureListener {
                Log.d("LOCAION", "Failure ${it.localizedMessage}")
            }
        } catch (e: Exception) {

        }
    }

    private fun initObservers() {
        viewModel.weatherData.observe(this) {
            if (it.ResponseCode == 200) {
                if (it.Result != null) {
                    precipProb=it.Result.list?.get(0)?.pop.toString()?:""
                    initGraphUi(it.Result)
                    Toast.makeText(this, "Success in fetching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.currentWeatherData.observe(this){
            if(it.ResponseCode==200){
                if(it.Result!=null){
                    initUi(it.Result)

                    animateCurve()
                }
            }
        }
    }


    private fun initUi(currentWeatherResponse: CurrenrWeatherResponse,){
        binding.apply {
            progressBar.visibility=View.GONE
            mainLayout.visibility= View.VISIBLE
            location.text = currentWeatherResponse.name?:""
            mainTemp.text=currentWeatherResponse.main.temp.toInt().toString()+"\u2103"
            weatherType.text = currentWeatherResponse.weather?.get(0)?.description
            feelsLikeTemp.text=currentWeatherResponse.main.feels_like.toInt().toString()+"\u2103"
            probPrec.text=precipProb+"%"
            sunsetTime.text=SimpleDateFormat("hh:mm a").format(Date(currentWeatherResponse.sys.sunset*1000))
            sunriseTime.text=SimpleDateFormat("hh:mm a").format(Date(currentWeatherResponse.sys.sunrise*1000))
            Glide.with(bigWeatherIcon).load("http://openweathermap.org/img/wn/${currentWeatherResponse.weather[0].icon}.png").into(bigWeatherIcon)
        }
    }
    private fun initGraphUi(weatherResponse: WeatherResponse) {
        binding.apply {
            val weatherList = weatherResponse.list?.subList(0, 8)
            val weatherTimeData = arrayListOf<WeatherTimeData>()
            var min=100
            var max=-1
            weatherList?.forEach { ele ->
                if (ele.dt != null) {
                     if(ele.main!!.temp_max!!>max){
                         max=ele.main!!.temp_max!!.toInt()
                     }
                     if(ele.main!!.temp_min!!<min){
                         min=ele.main!!.temp_min!!.toInt()
                     }
                    weatherTimeData.add(
                        WeatherTimeData(
                            time = formatTime(timeStamp = ele.dt),
                            icon = "http://openweathermap.org/img/wn/${ele.weather?.get(0)?.icon}",
                            temperature = "${ele.main?.temp?.toInt().toString()}\u00B0"
                        )
                    )
                }
            }
            maxTemp.text=max.toString()+"℃"
            minTemp.text=min.toString()+"℃"
            precipitationProbability.title.text = "Probability of precipitation"
            precipitationProbability.info.text = weatherResponse.list?.get(0)?.pop.toString()

            precipitation.title.text = "Precipitation"
            precipitation.info.text = "NA"

            wind.title.text = "Wind"
            wind.info.text = weatherResponse.list?.get(0)?.wind?.deg.toString()

            humidity.title.text = "Humidity"
            humidity.info.text = weatherResponse.list?.get(0)?.main?.humidity.toString()+"%"

            visibility.title.text = "Visibility"
            visibility.info.text ="${(weatherResponse.list?.get(0)?.visibility)?.div(1000)} Km"

            uv.title.text = "UV"
            uv.info.text = "NA"

            temperature.title.text = "Perceived temperature"
            temperature.info.text = weatherResponse.list?.get(0)?.main?.temp.toString()+"°C"

            pressure.title.text = "Pressure"
            pressure.info.text = weatherResponse.list?.get(0)?.main?.pressure.toString()+"hPa"


            adapter.addData(weatherTimeData)

            setUpChart(weatherResponse)
        }
    }

    private fun setUpRecyclerView() {
        binding.apply {
            adapter = WeatherTimeUpdateAdapter()
            timeRev.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            timeRev.adapter = adapter

            graphAdapter= WeatherGraphAdapter(this@MainActivity)
            graph.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            graph.adapter=graphAdapter
        }
    }

    private fun formatTime(timeStamp: Long): String {
        return SimpleDateFormat("hh a").format(Date(timeStamp * 1000)).toString()
    }

    private fun setUpChart(weatherResponse: WeatherResponse) {
        binding.apply {

            val day1=weatherResponse.list?.get(0)
            val day2 = weatherResponse.list?.get(9)
            val day3 = weatherResponse.list?.get(17)
            val day4 = weatherResponse.list?.get(25)
            val day5 = weatherResponse.list?.get(33)

            val weatherList = ArrayList<WeatherResponseItem>()
            weatherList.add(day1!!)
            weatherList.add(day2!!)

            weatherList.add(day3!!)

            weatherList.add(day4!!)

            weatherList.add(day5!!)

           graphAdapter.addData(weatherList)





        }
    }


    private fun animateCurve(){
        val path= Path().apply {
            arcTo(0f,0f,800f,800f,-180f,180f,true)
        }
        val animator=ObjectAnimator.ofFloat(binding.sunIcon,View.X,View.Y,path).apply {
            duration=4000
            start()
        }
    }



}