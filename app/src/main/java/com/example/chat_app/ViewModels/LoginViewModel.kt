package com.example.chat_app.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.Network.Network.User
import com.example.chat_app.Network.RetroFitHelper
import com.example.chat_app.Repository.RestRepo

class LoginViewModel: ViewModel() {
    private lateinit var repo:RestRepo
    init {
        repo= RestRepo.getInstance()
    }
    fun isUserExist(user: User){
        repo.isUserExist(user)
    }
    fun isUser(): MutableLiveData<User> {
        return repo.isUser()
    }
}