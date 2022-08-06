package com.example.chat_app

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app.Adapters.SearchUsersAdapter
import com.example.chat_app.ViewModels.ChatsActivityViewModel

class ChatsActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var userProfile:ImageView
    private lateinit var chatsViewModel: ChatsActivityViewModel
    private lateinit var searchUsersAdapter:SearchUsersAdapter
    private lateinit var recyclerView:RecyclerView
    private var newUserList= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        searchUsersAdapter= SearchUsersAdapter(newUserList)
        recyclerView=findViewById(R.id.allChats)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=searchUsersAdapter


        chatsViewModel=ViewModelProvider(this).get(ChatsActivityViewModel::class.java)
        Log.d("TAG", "onCreate: USER_ID"+intent.getIntExtra(MainActivity.USER_ID,-1))
        chatsViewModel.setUser(intent.getStringExtra(MainActivity.USER_ID),intent.getStringExtra(MainActivity.USER_NAME))


        val view: View? =layoutInflater.inflate(R.layout.custom_chats_action_bar,null)
        userProfile=view?.findViewById(R.id.user_profile)!!
        searchView= view?.findViewById(R.id.search_user)!!
        this.supportActionBar!!.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
        this.supportActionBar!!.setDisplayShowCustomEnabled(true)
        this.supportActionBar!!.setCustomView(view)


        searchView.setOnSearchClickListener {
            userProfile.visibility=View.GONE
        }
        searchView.setOnCloseListener (object :SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                userProfile.visibility=View.VISIBLE
                return false

            }

        })
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchUsersAdapter.filter.filter(newText)
                return false
            }

        })

    }
}