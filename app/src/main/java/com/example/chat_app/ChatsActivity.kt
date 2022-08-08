package com.example.chat_app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.Adapters.SearchUsersAdapter
import com.example.chat_app.Network.Network.User
import com.example.chat_app.ViewModels.ChatsActivityViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatsActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var userProfile:ImageView
    private lateinit var header:TextView
    private lateinit var chatsViewModel: ChatsActivityViewModel
    private lateinit var searchUsersAdapter:SearchUsersAdapter
    private lateinit var recyclerView:RecyclerView
    private var newUserList= ArrayList<User>()
    private lateinit var firebaseStorage:FirebaseStorage
    val db=Firebase.firestore


    private var id:String=""
    private var userProfilePicker=registerForActivityResult(StartActivityForResult()){
        var link: Any? =null
        if(it.resultCode==Activity.RESULT_OK){
            val data=it.data
            val imageInputStream=contentResolver.openInputStream(data?.data!!)
            val profilePic:Bitmap=BitmapFactory.decodeStream(imageInputStream)
            Glide.with(this).load(profilePic).into(userProfile)

            val bytes:ByteArrayOutputStream= ByteArrayOutputStream()
            profilePic.compress(Bitmap.CompressFormat.JPEG,50,bytes)
            val path : String=MediaStore.Images.Media.insertImage(contentResolver,profilePic,"user_profile","some")!!


            firebaseStorage.reference.child("profile pictures").putFile(Uri.parse(path))
                .addOnSuccessListener {
                    val url = it.metadata?.reference?.downloadUrl

                    Log.d("TAG", "id : "+id)

                    if (url != null) {
                        url.addOnCompleteListener(OnCompleteListener {
                                val some=it.result.toString()
                                Log.d("TAG", ": link"+some)
                                upadteUser(some)
                            })
                    }
                }





        }

    }

    private fun upadteUser(url: String) {
        val link = hashMapOf(
            "user_profile" to url
        )
        db.collection("USERS").document(id).set(link, SetOptions.merge())

    }

    companion object{
        private val USER_PROFILE_PICK_IMAGE:Int=1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        firebaseStorage=FirebaseStorage.getInstance()


        chatsViewModel=ViewModelProvider(this).get(ChatsActivityViewModel::class.java)
        Log.d("TAG", "onCreate: USER_ID"+intent.getStringExtra(MainActivity.USER_ID))
        chatsViewModel.setUser(intent.getStringExtra(MainActivity.USER_ID),intent.getStringExtra(MainActivity.USER_NAME))

        id= intent.getStringExtra(MainActivity.USER_ID).toString()
        searchUsersAdapter= SearchUsersAdapter(newUserList,id.toInt())
        recyclerView=findViewById(R.id.allChats)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=searchUsersAdapter


        userProfile=findViewById(R.id.user_profile)
        searchView= findViewById(R.id.search_user)
        header=findViewById(R.id.chats_activity_header)
        Firebase.firestore.collection("USERS").document(id).get().addOnSuccessListener {

//            Glide.with(this).load(it.get("user_profile")).into(userProfile)
            db.collection("USERS").document(id).get().addOnSuccessListener {
                val url=it.get("user_profile").toString()

                Glide.with(this).load(url).into(userProfile)
                Log.d("TAG", "onCreate: downloading"+url)
            }

        }
//        this.supportActionBar!!.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
//        this.supportActionBar!!.setDisplayShowCustomEnabled(true)
//        this.supportActionBar!!.setCustomView(view)

        userProfile.setOnClickListener {
            Log.d("TAG", "onCreate: userProfile clicked")
            val intent=Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            val inte:Intent=Intent.createChooser(intent,"Pick Image for User Profile")
            userProfilePicker.launch(inte)

        }


        searchView.setOnSearchClickListener {
            userProfile.visibility=View.GONE
            header.visibility=View.GONE
        }
        searchView.setOnCloseListener (object :SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                userProfile.visibility=View.VISIBLE
                header.visibility=View.VISIBLE
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