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
import androidx.core.content.res.ResourcesCompat.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.chat_app.Adapters.SearchUsersAdapter
import com.example.chat_app.Adapters.chatsAdapter
import com.example.chat_app.Database.FirestoreObject
import com.example.chat_app.Database.SqlUserEntity
import com.example.chat_app.Network.Network.User
import com.example.chat_app.ViewModels.ChatsActivityViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

class ChatsActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var userProfile:ImageView
    private lateinit var header:TextView
    private lateinit var chatsViewModel: ChatsActivityViewModel
    private lateinit var searchUsersAdapter:SearchUsersAdapter
    private lateinit var allFriends:RecyclerView
    private lateinit var allSearches:RecyclerView
    private var newUserList= ArrayList<User>()
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var friendsList:ArrayList<User>
    private lateinit var allFriendsChatsList:ArrayList<FirestoreObject>
    private val default_avatar="https://firebasestorage.googleapis.com/v0/b/chat-app-ab6f9.appspot.com/o/default_avatar.jpeg?alt=media&token=b75cb1a6-f21f-4377-b626-ddd0ff7789ca"
    private lateinit var allFriendsAdapter:SearchUsersAdapter
    private lateinit var allFriendsChatsAdapter:chatsAdapter
    val db=Firebase.firestore


    private var id:String=""
    private var userName:String=""
    private var userProfilePicker=registerForActivityResult(StartActivityForResult()){
        var link: Any? =null
        if(it.resultCode==Activity.RESULT_OK){
            val data=it.data
            val imageInputStream=contentResolver.openInputStream(data?.data!!)
            val profilePic:Bitmap=BitmapFactory.decodeStream(imageInputStream)
            val options=RequestOptions().override(200,200)

            Glide.with(this).apply { options }.load(profilePic).into(userProfile)

            val bytes:ByteArrayOutputStream= ByteArrayOutputStream()
            profilePic.compress(Bitmap.CompressFormat.PNG,100,bytes)
            val path : String=MediaStore.Images.Media.insertImage(contentResolver,profilePic,"user_profile","some")!!

            firebaseStorage.reference.child("profile pictures"+id).putFile(Uri.parse(path))
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
        Log.d("TAG", "upadteUser: profile link"+id)
        db.collection("USERS").document(id).set(link, SetOptions.merge())
    }

    companion object{
        private val USER_PROFILE_PICK_IMAGE:Int=1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        chatsViewModel=ViewModelProvider(this).get(ChatsActivityViewModel::class.java)
        Log.d("TAG", "onCreate: USER_ID"+intent.getStringExtra(MainActivity.USER_ID))
        chatsViewModel.setUser(intent.getStringExtra(MainActivity.USER_ID),intent.getStringExtra(MainActivity.USER_NAME))

        val bundle: Bundle? =intent.getBundleExtra(MainActivity.USER)
        val user: User? =bundle?.getParcelable<User>(MainActivity.USER)

        id= user?.userId.toString()
        userName=user?.username.toString()
        friendsList= ArrayList(1)
        allFriendsChatsList= ArrayList(1)
        allFriendsChatsAdapter=chatsAdapter(allFriendsChatsList,id.toInt())

        firebaseStorage=FirebaseStorage.getInstance()
        allFriends=findViewById(R.id.allChats)
        allSearches=findViewById(R.id.allSearches)
        allFriends.layoutManager=LinearLayoutManager(this)
        allSearches.layoutManager=LinearLayoutManager(this)
        allSearches.layoutManager=LinearLayoutManager(this)







        db.collection("USERS").document(id).get().addOnCompleteListener {
            if(it.isSuccessful){
                val user_profile=it.result.get("user_profile").toString()
                var firestoreObject:FirestoreObject
                Log.d("TAG", "onCreate: user_profile is "+user_profile)
                if(user_profile!=default_avatar ){
                    firestoreObject = FirestoreObject(userName,id,user_profile)
                }else{
                    firestoreObject= FirestoreObject(userName,id,default_avatar)
                }
                Log.d("TAG", "onCreate: firestore object is "+firestoreObject.toString())
                db.collection("USERS").document(id).set(firestoreObject)
            }
        }

        val data= hashMapOf(
            "userid" to id,
            "username" to userName,
            "user_profile" to default_avatar,
        )
//        db.collection("NEW").document().set(data, SetOptions.merge()).addOnSuccessListener {
//            Log.d("TAG", "onCreate: successfully added")
//        }


        Log.d("TAG", "onCreate: userId is $id and username is $userName")
        searchUsersAdapter= SearchUsersAdapter(newUserList,id.toInt())
        allFriends=findViewById(R.id.allChats)
        allFriends.layoutManager=LinearLayoutManager(this)
        allFriendsAdapter= SearchUsersAdapter(friendsList,id.toInt())
        allSearches.adapter=searchUsersAdapter
        allFriends.adapter=allFriendsChatsAdapter

        allFriends.layoutManager=LinearLayoutManager(this)


        userProfile=findViewById(R.id.user_profile)
        searchView= findViewById(R.id.search_user)
        header=findViewById(R.id.chats_activity_header)
        header.setTypeface(getFont(this,R.font.montserratbold))
        Firebase.firestore.collection("USERS").document(id).get().addOnSuccessListener {
            db.collection("USERS").document(id).get().addOnSuccessListener {
                val url=it.get("user_profile").toString()

                Glide.with(this).load(url).into(userProfile)
                Log.d("TAG", "onCreate: downloading"+url)
            }

        }


        userProfile.setOnClickListener {
            Log.d("TAG", "onCreate: userProfile clicked")
            val intent=Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            val inte:Intent=Intent.createChooser(intent,"Pick Image for User Profile")
            userProfilePicker.launch(inte)

        }

        Log.d("TAG", "onCreate: the id is "+id)
        db.collection("USERS").document(id).collection("friends").addSnapshotListener { value, error ->
            Log.d("TAG", "onCreate: friend complete listener size is "+value?.documents?.size)

            allFriendsChatsList.clear()
            value?.documents?.forEach {
                val id=it.get("friend")
                Log.d("TAG", "onCreate: friend"+it.get("friend"))
                db.collection("USERS").document(id.toString()).get().addOnCompleteListener {
                    Log.d("TAG", "onCreate: fetched username"+it.result.get("username"))
                    if(it.isSuccessful){
                        val obj=FirestoreObject(it.result.get("username").toString()
                            ,it.result.get("userid").toString()
                            ,it.result.get("user_profile").toString())
                        allFriendsChatsList.add(obj)
                        allFriendsChatsAdapter.notifyDataSetChanged()

                    }

                }
            }
        }


        searchView.setOnSearchClickListener {
            userProfile.visibility=View.GONE
            header.visibility=View.GONE
            allFriends.visibility=View.GONE
            allSearches.visibility=View.VISIBLE
            searchView.setMaxWidth(Integer.MAX_VALUE)
        }
        
        searchView.setOnCloseListener (object :SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                userProfile.visibility=View.VISIBLE
                header.visibility=View.VISIBLE
                allFriends.visibility=View.VISIBLE
                allSearches.visibility=View.GONE
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