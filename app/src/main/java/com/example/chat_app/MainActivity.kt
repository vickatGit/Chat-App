package com.example.chat_app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.chat_app.Adapters.ViewPagerAdapter
import com.example.chat_app.Database.SqlUserEntity
import com.example.chat_app.Database.UserLoginSignUpDatabase
import com.example.chat_app.Network.Network.User
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var tablayout:TabLayout
    private lateinit var viewpager:ViewPager2
    private lateinit var loginSignupAdapter:ViewPagerAdapter
    companion object{
        lateinit var  context:Context
        public var USER_ID="user_id"
        public var USER_NAME="user_name"
        public var USER="user"
        private var PERMISSION_REQUESTOR=12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context=this
//        UserLoginSignUpDatabase.getInstance(MainActivity.context)?.getUserLoginDao()?.insertUser(
//            SqlUserEntity(null, "guest","12"))
        val isStoragePermissionGranted=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(isStoragePermissionGranted==PackageManager.PERMISSION_GRANTED){
            proceed()
            Log.d("TAG", "onCreate: permission is Granted")
        }
        else {
            RequestForPermissions()
            Log.d("TAG", "onCreate: Requesting for permissions")
        }




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

    private fun RequestForPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE
                                                                    ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),PERMISSION_REQUESTOR)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSION_REQUESTOR) {
            if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED)
            {
                proceed()
            }
        }
    }

    private fun initialise() {
        tablayout=findViewById(R.id.login_signup_tab)
        viewpager=findViewById(R.id.login_signup_viewpager)
    }
    private fun proceed(){
        if(UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()?.dataId!=null){
//            val intent= Intent(this,ChatsActivity::class.java)
            val id=UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()?.dataId
            intent.putExtra(USER_NAME,UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()?.username)
            intent.putExtra(USER_ID,id.toString())
            val sqlUser=UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()
            val userId =sqlUser?.dataId
            Log.d("TAG", "onCreate: the iD That Cannot be cast is ${userId} and the or is ${userId.toString()}")
            val user=User(userId?.toInt(),sqlUser?.username.toString(),"",null)
            val bundle:Bundle= Bundle()
            bundle.putParcelable(USER,user)
            val intent:Intent= Intent(this, ChatsActivity::class.java)
            intent.putExtra(USER,bundle)
            startActivity(intent)
            finish()
        }
    }
}