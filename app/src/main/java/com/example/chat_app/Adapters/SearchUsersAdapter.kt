package com.example.chat_app.Adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.ChatActivity
import com.example.chat_app.MainActivity
import com.example.chat_app.Network.Network.User
import com.example.chat_app.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchUsersAdapter(var newUserList: ArrayList<User>, val userId: Int,val userName:String) : RecyclerView.Adapter<SearchUsersAdapter.SearchUserViewHolder>(), Filterable{


    val db=Firebase.firestore
    val searchedUserList=newUserList
    companion object{
        public var TAG="tag"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.chat_thumbnail_layout,parent,false)
        return SearchUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.userName.text=newUserList.get(position).username
        holder.userName.setTypeface(ResourcesCompat.getFont(holder.userName.context,R.font.montserratmedium))
        Glide.with(holder.userProfile.context).load(newUserList.get(position).password).into(holder.userProfile)
        holder.user.setOnClickListener{
            val intent = Intent(holder.user.context,ChatActivity::class.java)
            val bundle=Bundle()
            bundle.putParcelable("user",newUserList.get(position))
            intent.putExtra("user",bundle)
            Log.d(TAG, "onBindViewHolder: id is"+userId)
            intent.putExtra("userid",userId)
            intent.putExtra("username",userName)
            holder.user.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ")
        return newUserList.size
    }

    class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfile:ImageView=itemView.findViewById(R.id.thumb_user_profile)
        val userName:TextView=itemView.findViewById(R.id.thumb_user_name)
        val user:View=itemView
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            var filters = ArrayList<User>()
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                Log.d(TAG, "performFiltering: ")
                val data=hashMapOf(
                    "username" to "this works"
                )
                db.collection("USERS").whereEqualTo ("username",constraint.toString()).get().addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.d(TAG, "performFiltering: size of it ${it.result.documents.size}" )
                        it.result.documents.forEach{
                            Log.d(SearchUsersAdapter.TAG, "InFiltering: "+it.get("username"))

                            val us=User(it.get("userid").toString().toInt(),it.get("username").toString(),it.get("user_profile").toString(),it.get("user_token").toString())
                            if(us.userId!=userId) {
                                filters.add(us)
                            }
                            newUserList.add(us)
                            publishResults(constraint,FilterResults().apply { values=filters })
                        }
                    }
                    else{
                        Log.d(SearchUsersAdapter.TAG, "setUser: unsuccesful")
                    }
                }

                return FilterResults().apply { values=filters }


            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d(TAG, "publishResults: ")
                if (results?.values != null) {
                    newUserList.clear()
                    Log.d(TAG, "publishResults: "+results.values.toString())

                    newUserList.addAll(results.values as Collection<User>)
                    notifyDataSetChanged()
                }
                else{
                    newUserList.addAll(searchedUserList)
                    notifyDataSetChanged()
                }
            }

        }
    }


}