package com.example.chat_app.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SqlUserEntity {
    @PrimaryKey
    var id:Int=1
    lateinit var username:String
    lateinit var password:String

    constructor(id: Int?, username: String, password: String) {
        if (id != null) {
            this.id = id
        }
        this.username = username
        this.password = password
    }

    override fun toString(): String {
        return "SqlUserEntity(id=$id, username='$username', password='$password')"
    }

}