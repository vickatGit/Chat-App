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
import com.example.chat_app.ViewModels.SignUpViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupFragment : Fragment() {

    private lateinit var userNameContainer: TextInputLayout
    private lateinit var username: TextInputEditText
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var signUpBtn: Button
    private lateinit var confirmPasswordContainer:TextInputLayout
    private lateinit var confirmPassword:TextInputEditText
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(this).get(SignUpViewModel::class.java)
        val call=viewModel.getUser()
//        call.observe(this.requireActivity(), Observer {
//        })

        val observer= Observer<Int> {
            Log.d("TAG", "onViewCreated: livadata worked successfully and id is  $it \n")
            if(it!=null){
                UserLoginSignUpDatabase.getInstance(MainActivity.context)?.getUserLoginDao()
                    ?.insertUser(
                        SqlUserEntity(null, username.text.toString(),it.toString())
                    )
                Log.d("TAG",
                    "onViewCreated: in sqLite Database " + UserLoginSignUpDatabase.getInstance(this.requireContext())
                        ?.getUserLoginDao()?.getUser()
                )
                val intent=Intent(this.context,ChatsActivity::class.java)
                startActivity(intent)
            }
        }
        viewModel.getUser().observe(this.viewLifecycleOwner,observer)

        userNameContainer=view.findViewById(R.id.username_container)
        username=view.findViewById(R.id.username)
        passwordContainer=view.findViewById(R.id.password_container)
        password=view.findViewById(R.id.password)
        confirmPasswordContainer=view.findViewById(R.id.confirm_password_container)
        confirmPassword=view.findViewById(R.id.confirm_password)
        signUpBtn=view.findViewById(R.id.signup)


        signUpBtn.setOnClickListener {
            if (checkValidPassword(password.text.toString(), confirmPassword.text.toString())) {
                val user: User = User(null, username.text.toString(), password.text.toString())

                viewModel.createUser(user)
            }
        }
    }

    private fun checkValidPassword(password: String, confirmPassword: String): Boolean {
        if(password==confirmPassword)
            return true
        else
            return false
    }


}