package com.example.chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.chat_app.Network.Network.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    val db=Firebase.firestore

    private lateinit var message:EditText
    private lateinit var send:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        message=findViewById(R.id.message)
        send=findViewById(R.id.send)
        val bundle=intent.getBundleExtra("user")
        val user: User? =bundle?.getParcelable<User>("user")
        val id=intent.getStringExtra("userid")
        send.setOnClickListener {
            db.collection("USERS").document(user?.userId.toString()).collection("chats").document(user?.username.toString()).set(message.text.toString())
        }

    }
}