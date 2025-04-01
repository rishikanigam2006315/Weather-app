package com.example.weatherapp.ui.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherViewModel :ViewModel(){
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city : String){
        if(city.isBlank()){
            _weatherResult.value = NetworkResponse.Error("City name cannot be empty")
            return
        }
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "API Request: https://api.weatherapi.com/v1/current.json?key=${Constant.apikey}&q=$city")
                val response = weatherApi.getWeather(Constant.apikey, city)
                withContext(Dispatchers.Main) {
//                val response = weatherApi.getWeather(Constant.apikey, city)
//                Log.d("WeatherViewModel", "API Response: ${response.raw()}")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _weatherResult.value = NetworkResponse.Success(it)
                        } ?: run {
                            _weatherResult.value = NetworkResponse.Error("Empty response")
                        }
                    } else {
                        _weatherResult.value =
                            NetworkResponse.Error("API Error: %{response.code()} - ${response.message()}")

                    }
                }
            }
            catch (e : Exception){
                Log.e("WeatherViewModel", "Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data: ${e.localizedMessage}")
                }
//                Log.e("WeatherViewModel", "API Call Failed", e)
//                _weatherResult.value = NetworkResponse.Error("Failed to load data: ${e.message}")
            }
        }



    }
}