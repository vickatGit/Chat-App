package com.example.chat_app.Models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageModel(val message:String,val from:Int,val to:Int,val createdAt:Timestamp) : Parcelable
