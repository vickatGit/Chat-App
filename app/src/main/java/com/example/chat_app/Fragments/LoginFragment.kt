package com.example.chat_app.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app.ChatsActivity
import com.example.chat_app.Database.SqlUserEntity
import com.example.chat_app.Database.UserLoginSignUpDatabase
import com.example.chat_app.MainActivity
import com.example.chat_app.Network.Network.User
import com.example.chat_app.R
import com.example.chat_app.ViewModels.LoginViewModel
import com.example.chat_app.ViewModels.SignUpViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var userNameContainer:TextInputLayout
    private lateinit var username:TextInputEditText
    private lateinit var passwordContainer:TextInputLayout
    private lateinit var password:TextInputEditText
    private lateinit var loginBtn:Button
    private lateinit var loginViewModel:LoginViewModel
    private val SALT_ROUNDS=10    // cost factor must be Between 4 and 31


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        val observer= Observer<User> {
            Log.d("TAG", "Login View Observer : "+it.userId  )
            if(it!=null){
                UserLoginSignUpDatabase.getInstance(MainActivity.context)?.getUserLoginDao()?.insertUser(
                    SqlUserEntity(null, username.text.toString(),it.toString()))
                Log.d("TAG","onViewCreated: in sqLite Database " + UserLoginSignUpDatabase.getInstance(this.requireContext())?.getUserLoginDao()?.getUser())
                val intent= Intent(this.context, ChatsActivity::class.java)
                startActivity(intent)
            }
        }
        loginViewModel.isUser().observe(this.viewLifecycleOwner,observer)
        initialise(view)

        loginBtn.setOnClickListener {
            loginViewModel.isUserExist(User(null, username.text.toString(),password.text.toString()))
        }







    }
    fun initialise(view:View){
        userNameContainer=view.findViewById(R.id.username_container)
        username=view.findViewById(R.id.username)
        passwordContainer=view.findViewById(R.id.password_container)
        password=view.findViewById(R.id.password)
        loginBtn=view.findViewById(R.id.login)
    }
}