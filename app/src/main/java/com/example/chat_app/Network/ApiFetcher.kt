package com.example.chat_app.Network

import com.example.chat_app.Network.Network.User
import com.example.chat_app.NotificationService.ChatNotification
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiFetcher {

    @POST("addUser")
    fun createUser(@Body user:User):Call<Int>

    @POST("isUser")
    fun isUser(@Body user:User):Call<User>

    @Headers("Authorization:key=AAAAKnUoa5s:APA91bF1dNvxs0cZxGPLzswU-lkLXFzANl5vkHNH8Gto4ThQMT_rwb8RLBZeLAFnDTFMlCl1vG48l3rv3GrNBKzZtoH5v3XM1IJkJ2BQA-o1UXy2kM3H1WxBAJcMiPDzFzeTWmcMBh11")
    @POST("send")
    fun notify(@Body not:ChatNotification):Call<String>
}