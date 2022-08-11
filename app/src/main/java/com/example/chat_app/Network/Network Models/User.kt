package com.example.chat_app.Network.Network

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(

    @Nullable
    var userId:Int?,
    val username:String,
    @Ignore
    val password:String,
    val userToken:String?

) : Parcelable
