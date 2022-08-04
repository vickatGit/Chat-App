package com.example.chat_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.chat_app.Adapters.ViewPagerAdapter
import com.example.chat_app.Database.UserLoginSignUpDatabase
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tablayout:TabLayout
    private lateinit var viewpager:ViewPager2
    private lateinit var loginSignupAdapter:ViewPagerAdapter
    companion object{
        lateinit var  context:Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()?.dataId!=null){
            val intent= Intent(this,ChatsActivity::class.java)
            startActivity(intent)
        }
        context=this

        initialise()
        loginSignupAdapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
        viewpager.adapter=loginSignupAdapter
        TabLayoutMediator(tablayout,viewpager){tab,position->
            if (position==0)
                tab.setText("Login")
            else
                tab.setText("Signup")

        }.attach()


    }

    private fun initialise() {
        tablayout=findViewById(R.id.login_signup_tab)
        viewpager=findViewById(R.id.login_signup_viewpager)
    }
}