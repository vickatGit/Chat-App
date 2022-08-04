package com.example.chat_app.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.chat_app.Network.ApiFetcher
import com.example.chat_app.Network.Network.User
import com.example.chat_app.Network.RetroFitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class RestRepo {
    private lateinit var retro:ApiFetcher
    companion object{
        var isUserCreated : MutableLiveData<Int> = MutableLiveData()
        var isUserExist : MutableLiveData<User> = MutableLiveData()
        fun getInstance():RestRepo{
            return RestRepo()
        }
    }
    private constructor() {
        retro= RetroFitHelper.getInstance().create(ApiFetcher::class.java)
    }
    fun createUser(user: User): MutableLiveData<Int> {
        retro.createUser(user).enqueue(object :Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                Log.d("TAG", "onResponse: User Created Successfully"+response.body())
                isUserCreated.postValue(response.body())
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("TAG", "onFailure: cant create user "+t.message)
            }

        })
        return isUserCreated

    }

    fun getUser(): MutableLiveData<Int> {
        return isUserCreated
    }

    fun isUserExist(user: User) {
        retro.isUser(user).enqueue(object:Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.body()!=null){
                    isUserExist.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("TAG", "onFailure: login "+t.localizedMessage)
            }

        })
    }
    fun isUser(): MutableLiveData<User> {
        return isUserExist
    }
}