package com.example.chat_app.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat_app.Network.Network.User


@Dao
interface UserLoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: SqlUserEntity)

    @Query("SELECT * FROM SqlUserEntity")
    fun getUser():SqlUserEntity
}