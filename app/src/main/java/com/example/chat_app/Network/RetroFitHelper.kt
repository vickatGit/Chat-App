package com.example.chat_app.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitHelper {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("http://192.168.43.103:9000/Users/").addConverterFactory(GsonConverterFactory.create()).build()

    }
}