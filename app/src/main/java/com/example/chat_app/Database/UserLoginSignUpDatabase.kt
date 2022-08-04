package com.example.chat_app.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [SqlUserEntity::class], views = [], version = 1,exportSchema = false)
abstract class UserLoginSignUpDatabase : RoomDatabase() {
    abstract fun getUserLoginDao():UserLoginDao

    companion object{
        @Volatile
        var userLoginSignUpDatabase: UserLoginSignUpDatabase? =null
        @Synchronized
        fun getInstance(context: Context):UserLoginSignUpDatabase?{
            if(userLoginSignUpDatabase==null) {

                userLoginSignUpDatabase = Room.databaseBuilder(
                    context,
                    UserLoginSignUpDatabase::class.java,
                    "UserLoginSignUpDatabase.db"
                ).allowMainThreadQueries().build()
            }

            return userLoginSignUpDatabase
        }
    }


}