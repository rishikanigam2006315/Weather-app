package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    val response = weatherApi.getWeather(Constant.apikey, city)
//    Log.d("WeatherViewModel", "API Request: https://api.weatherapi.com/v1/current.json?key=${Constant.apikey}&q=$city")


    private const val BASE_URL = "https://api.weatherapi.com/";

    private fun getInstance() : Retrofit{

       return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi: WeatherApi = getInstance().create(WeatherApi::class.java)

}

