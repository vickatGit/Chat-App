package com.example.chat_app.Adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.ChatActivity
import com.example.chat_app.Database.FirestoreObject
import com.example.chat_app.Network.Network.User
import com.example.chat_app.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class chatsAdapter(val friends:ArrayList<FirestoreObject>,val userId:Int) : RecyclerView.Adapter<chatsAdapter.chatsThumbHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatsThumbHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.chats_thumbnail,parent,false)
        return chatsThumbHolder(view)
    }

    override fun onBindViewHolder(holder: chatsThumbHolder, position: Int) {

        Firebase.firestore.collection("USERS").document(userId.toString())
            .collection("friends").document(friends.get(position).userid).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val latest_message=it.result.get("latest_message").toString()
                    holder.latestMessage.text=latest_message
                }
            }
        Log.d("TAG", "onBindViewHolder: chatsAdapter")
        holder.userName.text=friends.get(position).username
        Glide.with(holder.user_profile.context).load(friends.get(position).user_profile).into(holder.user_profile)
        holder.user.setOnClickListener{
            val intent = Intent(holder.user.context, ChatActivity::class.java)
            val bundle= Bundle()
            val friend=friends.get(position)
            val user= User(friend.userid.toInt(),friend.username,friend.user_profile.toString(),friend.user_token)
            bundle.putParcelable("user",user)
            intent.putExtra("user",bundle)
            Log.d("TAG", "onBindViewHolder: in chatsAdapter id is"+userId)
            intent.putExtra("user_token",friends.get(position).user_token)
            intent.putExtra("userid",userId)
            holder.user.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return friends.size
    }
    class chatsThumbHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val user_profile:ImageView=itemView.findViewById(R.id.thumb_user_profile)
        val userName:TextView=itemView.findViewById(R.id.thumb_user_name)
        val latestMessage:TextView=itemView.findViewById(R.id.latest_message)
        val user:View=itemView
    }
}