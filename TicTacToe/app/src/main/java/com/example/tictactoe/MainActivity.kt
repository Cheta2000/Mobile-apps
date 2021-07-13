package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN=124
    private lateinit var myRef:DatabaseReference
    private lateinit var myDatabase:FirebaseDatabase
    private lateinit var myUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    fun click1(view: View) {
        val myIntent= Intent(this,OpponentsActivity::class.java)
        myIntent.putExtra("user",myUser)
        startActivityForResult(myIntent,123)
    }

    fun click2(view: View){
        val myIntent= Intent(this,Game5::class.java)
        startActivityForResult(myIntent,123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val score=findViewById<TextView>(R.id.textView2)
        if(requestCode==123){
            if(data!=null) {
                val winner = data.getStringExtra("winner")
                score.text = "Last game: " + winner
            }
            else{
                score.text="Last game: no result"
            }
        }
        if(requestCode==RC_SIGN_IN){
            if(data!=null){
                val user =Firebase.auth.currentUser
                myUser=User(user!!.uid,user!!.displayName,user!!.email)
            }
            else{
                finish()
            }
        }
    }
}