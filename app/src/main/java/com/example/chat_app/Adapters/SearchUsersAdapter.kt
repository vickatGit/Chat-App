package com.example.chat_app.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app.Network.Network.User
import com.example.chat_app.R
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchUsersAdapter(var newUserList: ArrayList<String>) : RecyclerView.Adapter<SearchUsersAdapter.SearchUserViewHolder>(), Filterable{


    val db=Firebase.firestore
    companion object{
        public var TAG="tag"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.chat_thumbnail_layout,parent,false)
        return SearchUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.userName.text=newUserList.get(position)
    }

    override fun getItemCount(): Int {
        return newUserList.size
    }

    class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfile:ImageView=itemView.findViewById(R.id.thumb_user_profile)
        val userName:TextView=itemView.findViewById(R.id.thumb_user_name)
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            var filters = ArrayList<String>()
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
                            filters.add(it.get("username").toString())
                            newUserList.add(it.get("username").toString())
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

                    newUserList.addAll(results.values as Collection<String>)
                    notifyDataSetChanged()
                }
            }

        }
    }


}