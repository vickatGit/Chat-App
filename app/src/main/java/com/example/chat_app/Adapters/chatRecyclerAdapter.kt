package com.example.chat_app.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.ResourcesCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app.Models.MessageModel
import com.example.chat_app.R

class chatRecyclerAdapter(val messages: List<MessageModel>, val id: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        if(viewType==1){
            val view=inflater.inflate(R.layout.outgoing_message_layout,parent,false)
            return outgoingMessageViewHolder(view)
        }
        else{
            val view=inflater.inflate(R.layout.incoming_message_layout,parent,false)
            return incomingMessageHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        if(messages.get(position).from==id) {
            val outholder:outgoingMessageViewHolder= holder as outgoingMessageViewHolder
            outholder.outgoingMessage.setTypeface(getFont(outholder.outgoingMessage.context,R.font.montserratmedium))
            outholder.outgoingMessage.text=messages.get(position).message
        }
        else{
            val incHolder:incomingMessageHolder= holder as incomingMessageHolder
            incHolder.incomingMessage.setTypeface(getFont(incHolder.incomingMessage.context,R.font.montserratmedium))
            incHolder.incomingMessage.text=messages.get(position).message
        }
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: ")
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        if(messages.get(position).from==id)
            return 1
        else
            return 0
    }
    class incomingMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val incomingMessage:TextView=itemView.findViewById(R.id.inc_message)

    }
    class outgoingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val outgoingMessage:TextView=itemView.findViewById(R.id.out_message)
    }

}