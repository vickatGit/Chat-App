package com.example.chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.res.ResourcesCompat.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.Adapters.chatRecyclerAdapter
import com.example.chat_app.Models.MessageModel
import com.example.chat_app.Network.Network.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    val db=Firebase.firestore

    private lateinit var message:EditText
    private lateinit var userName:TextView
    private lateinit var userProfile:ImageView
    private lateinit var back:ImageView
    private lateinit var send:FloatingActionButton
    private lateinit var chats:RecyclerView
    private lateinit var messages:ArrayList<MessageModel>
    private lateinit var chatAdapter:chatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        messages= ArrayList(1)

        val bundle=intent.getBundleExtra("user")
        val user: User? =bundle?.getParcelable<User>("user")
        val id=intent.getIntExtra("userid",-1)
        Log.d("TAG", "onCreate: user to send"+id)
        message=findViewById(R.id.message)
        send=findViewById(R.id.send)
        userProfile=findViewById(R.id.user_profile)
        userName=findViewById(R.id.username)
        back=findViewById(R.id.back)
        chats=findViewById(R.id.chats)

        message.setTypeface(getFont(this,R.font.montserratmedium))
        userName.setTypeface(getFont(this,R.font.montserratmedium))
        userName.text=user?.username
        Glide.with(this).load(user?.password).into(userProfile)

        chats.layoutManager=LinearLayoutManager(this)
        chatAdapter=chatRecyclerAdapter(messages,id)
        chats.adapter=chatAdapter


        val db=Firebase.firestore
        db.collection("MESSAGES").whereIn("from" , listOf(id,user!!.userId)).addSnapshotListener(
            EventListener { value, error ->
                Log.d("TAG", "onCreate: in snapshot listener")
                value?.documentChanges?.listIterator()?.forEach {
                    val message=MessageModel(it.document.get("message").toString()
                        ,it.document.get("from").toString().toInt()
                        ,it.document.get("to").toString().toInt(),it.document.getTimestamp("createdAt")!!)
                    Log.d("TAG", "onCreate: ChatsActivity message "+message.toString())
                    messages.add(message)

                }
                    messages.sortBy { it.createdAt }
                    chatAdapter.notifyDataSetChanged()
                chats.smoothScrollToPosition(chatAdapter.itemCount-1)
            })
        back.setOnClickListener {
            finish()
        }
        send.setOnClickListener {
            val message=MessageModel(message.text.toString(),id!!.toInt(),user!!.userId!!,Timestamp(Date()))
            db.collection("MESSAGES").document().set(message).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("TAG", "onCreate: "+it.result)
                    }
                    else
                        Log.d("TAG", "onCreate: failed ro store meaage"+it.exception)
                })


        }

    }
}