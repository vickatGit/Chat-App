package com.example.chat_app.NotificationService

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotifierService : FirebaseMessagingService() {
    companion object{
        private val TAG="tag"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.notification!=null){
            Log.d(TAG, "onMessageReceived: type is notification")
        }
        if(remoteMessage.data.size>0){
            Log.d(TAG, "onMessageReceived: type is data notification")
        }
    }
}