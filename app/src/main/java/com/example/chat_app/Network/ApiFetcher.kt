package com.example.chat_app.Network

import com.example.chat_app.Network.Network.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiFetcher {

    @POST("addUser")
    fun createUser(@Body user:User):Call<Int>

    @POST("isUser")
    fun isUser(@Body user:User):Call<User>
}