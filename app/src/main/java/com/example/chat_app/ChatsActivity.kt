package com.example.chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView

class ChatsActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var userProfile:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        supportActionBar?.setCustomView(R.layout.custom_chats_action_bar)
        val view: View? =supportActionBar?.customView
        searchView= view?.findViewById(R.id.search_user)!!
        userProfile=view?.findViewById(R.id.user_profile)!!





    }
}