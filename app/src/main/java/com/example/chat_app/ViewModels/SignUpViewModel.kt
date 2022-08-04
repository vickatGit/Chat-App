package com.example.chat_app.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.Network.Network.User
import com.example.chat_app.Repository.RestRepo


class LoginViewModel: ViewModel() {


    private lateinit var repo:RestRepo
    public  var users:MutableLiveData<Int> = MutableLiveData()

    init {
        repo= RestRepo()
    }
    fun createUser(user:User): MutableLiveData<Int> {
        users=repo.createUser(user)
        return users
    }
    fun getUser(): MutableLiveData<Int> {
        users=repo.getUser()
        return users
    }

}