package com.example.chat_app.NotificationService

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.chat_app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotifierService : FirebaseMessagingService() {
    companion object{
        private val TAG="tag"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    @SuppressLint("WrongConstant")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.notification!=null){
            val notifictionManager:NotificationManager= getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val not=NotificationCompat.Builder(this).setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setSmallIcon(R.drawable.chat_app_logo).setPriority(NotificationCompat.PRIORITY_HIGH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "onMessageReceived: type is notification ${remoteMessage.notification!!.title} and description is ${remoteMessage.notification!!.body}")
                val note=NotificationChannel("123","message",NotificationManager.IMPORTANCE_MAX)

                notifictionManager.createNotificationChannel(note)
                not.setChannelId("123")

                notifictionManager.notify(123,not.build())
            }
            else{
                notifictionManager.notify(123,not.build())
            }
        }
        if(remoteMessage.data.size>0){
            Log.d(TAG, "onMessageReceived: type is data notification")
        }
    }
}