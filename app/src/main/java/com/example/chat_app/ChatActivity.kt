package com.example.chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var messageInput:EditText
    private lateinit var userName:TextView
    private lateinit var userProfile:ImageView
    private lateinit var back:ImageView
    private lateinit var send:FloatingActionButton
    private lateinit var chats:RecyclerView
    private lateinit var messages:ArrayList<MessageModel>
    private lateinit var recieverMessages:ArrayList<MessageModel>
    private lateinit var chatAdapter:chatRecyclerAdapter
    private lateinit var friends:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        messages= ArrayList(1)
        recieverMessages= ArrayList(1)
        friends=ArrayList(1)

        val bundle=intent.getBundleExtra("user")
        val user: User? =bundle?.getParcelable<User>("user")
        val id=intent.getIntExtra("userid",-1)
        Log.d("TAG", "onCreate: user to send"+id)
        messageInput=findViewById(R.id.message)
        send=findViewById(R.id.send)
        userProfile=findViewById(R.id.user_profile)
        userName=findViewById(R.id.username)
        back=findViewById(R.id.back)
        chats=findViewById(R.id.chats)

        messageInput.setTypeface(getFont(this,R.font.montserratmedium))
        userName.setTypeface(getFont(this,R.font.montserratmedium))
        userName.text=user?.username
        Glide.with(this).load(user?.password).into(userProfile)

        chats.layoutManager=LinearLayoutManager(this)
        chatAdapter=chatRecyclerAdapter(messages,id)
        chats.adapter=chatAdapter


        val db=Firebase.firestore

        db.collection("MESSAGES").whereIn("from" , listOf(id,user!!.userId)).whereEqualTo("to",id).addSnapshotListener(
            EventListener { value, error ->
                Log.d("TAG", "onCreate: in snapshot listener  reciever id is $id and sender id is ${user.userId} "+value?.documents?.size)
                recieverMessages.clear()
                value?.documents?.listIterator()?.forEach {
                    val message=MessageModel(it.get("message").toString()
                        ,it.get("from").toString().toInt()
                        ,it.get("to").toString().toInt(),it.getTimestamp("createdAt")!!)
                    Log.d("TAG", "onCreate: ChatsActivity message "+message.toString())
                    recieverMessages.add(message)

                }
                messages.addAll(recieverMessages)
                messages.sortBy { it.createdAt }
                chatAdapter.notifyDataSetChanged()
                if(recieverMessages.size>=0) {
                    Log.d("TAG", "onCreate: size of messages  "+chatAdapter.itemCount)
//                    chats.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            })
        db.collection("MESSAGES").whereIn("from" , listOf(id,user!!.userId)).whereEqualTo("to",user!!.userId).addSnapshotListener(
            EventListener { value, error ->
                Log.d("TAG", "onCreate: in snapshot listener")
                messages.clear()
                value?.documents?.listIterator()?.forEach {
                    val message=MessageModel(it.get("message").toString()
                        ,it.get("from").toString().toInt()
                        ,it.get("to").toString().toInt(),it.getTimestamp("createdAt")!!)
                    Log.d("TAG", "onCreate: ChatsActivity message "+message.toString())
                    messages.add(message)

                }

                    messages.addAll(recieverMessages)
                    messages.sortBy { it.createdAt }
                    chatAdapter.notifyDataSetChanged()
                if(messages.size>=0) {
                    Log.d("TAG", "onCreate: size of messages  "+chatAdapter.itemCount)
//                    chats.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            })


        db.collection("USERS").document(id.toString()).get().addOnCompleteListener(
            OnCompleteListener {
                friends = it.result.data?.get("friends") as ArrayList<String>
                friends.add(id.toString())

            })

        db.collection("USERS").document(id.toString()).update("friends",friends).addOnCompleteListener(
            OnCompleteListener {
                Log.d("TAG", "onCreate: friend added successfully")
            })
        back.setOnClickListener {
            finish()
        }
        send.setOnClickListener {
            val message=MessageModel(messageInput.text.toString(),id!!.toInt(),user!!.userId!!,Timestamp(Date()))
            messageInput.setText("")
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