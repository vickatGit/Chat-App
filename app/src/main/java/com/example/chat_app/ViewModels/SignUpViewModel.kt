package com.example.chat_app.ViewModels

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.Network.Network.User
import com.example.chat_app.Repository.RestRepo
import java.util.logging.Handler


class SignUpViewModel: ViewModel() {


    private lateinit var repo:RestRepo
    public  var users:MutableLiveData<Int> = MutableLiveData()

    init {
        repo= RestRepo.getInstance()
    }
    companion object{

    }
    fun createUser(user: User): MutableLiveData<Int> {
        users.postValue(repo.createUser(user).value)
        while(users==null){

        }
        Log.d("TAG", "create user viewmodel"+users.value)
        return users
    }
    fun getUser(): MutableLiveData<Int> {
        return repo.getUser()
    }

}