package com.example.chat_app.Database

class FirestoreObject {
    public lateinit var username:String
    public lateinit var userid:String
    public var user_profile:String?=""

    constructor(
        username: String,
        userid: String,
        user_profile: String?,
    ) {
        this.username = username
        this.userid = userid
        this.user_profile = user_profile
    }

    override fun toString(): String {
        return "FirestoreObject(username='$username', userid='$userid', user_profile=$user_profile)"
    }

}