package com.example.chat_app.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatsActivityViewModel : ViewModel() {
    private val db=Firebase.firestore
    fun setUser(userId: String?, userName: String?){
        val data=hashMapOf(
            "username" to userName,
            "userid" to userId
        )
        db.collection("USERS").document(userId.toString()).set(data, SetOptions.merge())


    }

    fun userInfo(userId: Int, userName: String?) {

    }

    init {

    }
}