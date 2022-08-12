package com.example.chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.core.content.res.ResourcesCompat.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.Adapters.chatRecyclerAdapter
import com.example.chat_app.Models.MessageModel
import com.example.chat_app.Network.ApiFetcher
import com.example.chat_app.Network.Network.User
import com.example.chat_app.NotificationService.ChatNotification
import com.example.chat_app.NotificationService.Notify
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    val db=Firebase.firestore
    private var chatHashes:HashMap<String,MessageModel> = HashMap()

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
        val user_token=intent.getStringExtra("user_token")
        val username=intent.getStringExtra("username")
        Log.d("TAG", "onCreate: user to send $id  and token is $user_token")
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
//                recieverMessages.clear()
                messages.clear()
                value?.documents?.listIterator()?.forEach {
                    val message=MessageModel(it.get("message").toString()
                        ,it.get("from").toString().toInt()
                        ,it.get("to").toString().toInt(),it.getTimestamp("createdAt")!!,it.get("messageid").toString())
//                    Log.d("TAG", "onCreate: ChatsActivity message "+message.toString())
//                    recieverMessages.add(message)
                    chatHashes.put(message.messageid,message)
                    Log.d("messages", "onCreate: reciever messages"+message)

                }
                messages.addAll(chatHashes.values)
                messages.sortBy { it.createdAt }
                chatAdapter.notifyDataSetChanged()
                if(chatAdapter.getItemCount() >=1) {
                    Log.d("TAG", "onCreate: size of messages  "+chatAdapter.itemCount)
                    chats.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            })
        db.collection("MESSAGES").whereIn("from" , listOf(id,user!!.userId)).whereEqualTo("to",user!!.userId).addSnapshotListener(
            EventListener { value, error ->
                Log.d("TAG", "onCreate: in snapshot listener")
                messages.clear()
                value?.documents?.listIterator()?.forEach {
                    val message=MessageModel(it.get("message").toString()
                        ,it.get("from").toString().toInt()
                        ,it.get("to").toString().toInt(),it.getTimestamp("createdAt")!!,it.get("messageid").toString())
//                    Log.d("TAG", "onCreate: ChatsActivity message "+message.toString())
//                    messages.add(message)
                    chatHashes.put(message.messageid,message)
                    Log.d("messages", "onCreate: sender Messages"+message)

                }

                    messages.addAll(chatHashes.values)
                    messages.sortBy { it.createdAt }
                    chatAdapter.notifyDataSetChanged()
//                if(messages.size>=0) {
//                    Log.d("TAG", "onCreate: size of messages  "+chatAdapter.itemCount)
                    if(chatAdapter.itemCount>=1) {
                        Log.d("TAG", "onCreate: scrolled")
                        chats.smoothScrollToPosition(chatAdapter.itemCount - 1)
//                    }
                }
            })

        val som= hashMapOf(
            "friend" to user.userId.toString()
        )
        db.collection("USERS").document(id.toString()).collection("friends").document(user.userId.toString())
            .set(som, SetOptions.merge()).addOnSuccessListener {
                Log.d("TAG", "onCreate: friend added successfully")
            }

//        db.collection("USERS").document(id.toString()).get().addOnCompleteListener(
//            OnCompleteListener {
//                val fris=it.result.get("friends") as ArrayList<String>
//                fris.iterator().forEach {
//
//                }
//
//                friends = it.result.data?.get("friends") as Array<String>
//                friends.add(id.toString())
//
//            })

//        db.collection("USERS").document(id.toString()).update()
//        db.collection("USERS").document(id.toString()).update("friends",friends).addOnCompleteListener(
//            OnCompleteListener {
//                Log.d("TAG", "onCreate: friend added successfully")
//            })
        back.setOnClickListener {
            finish()
        }
        send.setOnClickListener {

            val retro= Retrofit.Builder().baseUrl("https://fcm.googleapis.com/fcm/").addConverterFactory(GsonConverterFactory.create()).build().create(ApiFetcher::class.java)
            val note=ChatNotification(user.userToken.toString(), Notify(username.toString(),messageInput.text.toString()))
            val gson:Gson= Gson()
            Log.d("TAG", "onCreate: to json is "+gson.toJson(note))
            retro.notify(note).enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    Log.d("TAG", "onResponse: notification is successful"+response.body())




                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("TAG", "onFailure: to send notification"+t.localizedMessage)

                }



            })
            val ref=db.collection("MESSAGES").document()
            Log.d("real", "onCreate: a real message "+messageInput.text)
            val message=MessageModel(messageInput.text.toString(),id!!.toInt(),user!!.userId!!,Timestamp(Date()),ref.toString())
            db.collection("MESSAGES").document(ref.toString()).set(message).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("TAG", "onCreate: "+it.result)
                        db.collection("USERS").document(user.userId.toString()).collection("friends").document(id.toString()).set(hashMapOf("latest_message" to message.message),
                            SetOptions.merge()).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Log.d("TAG", "onCreate: latest message is successful")
                                }
                                else
                                    Log.d("TAG", "onCreate: latest message is unsuccessful")
                        }
                        db.collection("USERS").document(id.toString()).collection("friends").document(user.userId.toString()).set(hashMapOf("latest_message" to message.message),
                            SetOptions.merge()).addOnCompleteListener {
                            if(it.isSuccessful){
                                Log.d("TAG", "onCreate: latest message is successful")
                            }
                            else
                                Log.d("TAG", "onCreate: latest message is unsuccessful")
                        }

                    }
                    else
                        Log.d("TAG", "onCreate: failed ro store meaage"+it.exception)
                })



        }

    }
}