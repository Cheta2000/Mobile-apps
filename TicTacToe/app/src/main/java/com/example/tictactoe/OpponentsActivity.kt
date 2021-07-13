package com.example.tictactoe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import kotlin.random.Random

class OpponentsActivity : AppCompatActivity(),RecyclerClick {

    private lateinit var myDatabase: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private var user:User?=null
    private var askedUser:User?=null
    private lateinit var recyclerView: RecyclerView
    private val users=arrayListOf<User>()
    private var dialog: AlertDialog?=null
    private var dialog2: AlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opponents)
        myDatabase = Firebase.database
        myRef = myDatabase.getReference("3x3").child("users")
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = CustomAdapter(users,myRef,this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        user = intent.getParcelableExtra("user")
        myRef.child(user!!.name!!).setValue(user)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (postSnapshot in dataSnapshot.children) {
                    users.add(postSnapshot.getValue<User>()!!)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        val myRef2=myRef.child(user!!.name!!).child("askingUser")
        myRef2.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val asking=snapshot.getValue<String>()
                var hisName=""
                for(user in users){
                    if(asking==user.id){
                        hisName=user.name!!
                    }
                }
                if(asking!=null){
                    if(asking!!.startsWith("GAME:")) {
                        val myIntent= Intent(this@OpponentsActivity,Game3::class.java)
                        myIntent.putExtra("id",asking)
                        myIntent.putExtra("user1",user!!.name)
                        myIntent.putExtra("user2",askedUser!!.name)
                        startActivityForResult(myIntent,125)
                    }
                    else if(asking=="NO"){
                        dialog?.hide()
                        myRef2.setValue(null)
                    }
                    else if(asking=="STOP"){
                        dialog2?.hide()
                        myRef.child(hisName).child("askingUser").setValue(null)
                    }
                    else{
                        val hisRef=myRef.child(hisName).child("askingUser")
                        val builder=AlertDialog.Builder(this@OpponentsActivity)
                        builder.apply{
                            setTitle("Invitation")
                            setMessage("User $hisName invites you to game!")
                            setPositiveButton("Play"){_,_->
                                val myName=user!!.name
                                var gameName="GAME: $myName-$hisName"
                                val start= Random.nextInt(10)
                                if (start < 5) {
                                    gameName+="X"
                                } else {
                                   gameName+="O"
                                }
                                val who= Random.nextInt(10)
                                if (start < 5) {
                                    hisRef.setValue(gameName+"X")
                                    gameName+="O"
                                } else {
                                    hisRef.setValue(gameName+"O")
                                    gameName+="X"
                                }
                                val myIntent= Intent(this@OpponentsActivity,Game3::class.java)
                                myIntent.putExtra("id",gameName)
                                myIntent.putExtra("user1",myName)
                                myIntent.putExtra("user2",hisName)
                                startActivityForResult(myIntent,125)
                            }
                            setNegativeButton("Dismiss"){_,_->
                                hisRef.setValue("NO")
                                myRef2.setValue(null)
                            }
                        }
                        dialog2=builder.create()
                        try {
                            dialog2?.show()
                        }
                        catch (exception:Exception) {
                            //use a log message
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            val score=data.getStringExtra("winner")
            val myIntent=Intent()
            myIntent.putExtra("winner",score)
            setResult(Activity.RESULT_OK,myIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.hide()
        myRef.child(user!!.name!!).setValue(user)
    }

    override fun onStop() {
        super.onStop()
        myRef.child(user!!.name!!).setValue(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        myRef.child(user!!.name!!).setValue(null)
    }

    override fun onClick(position: Int) {
        askedUser=users[position]
        if(user!!.id!= askedUser!!.id) {
            myRef.child(askedUser!!.name!!).child("askingUser").setValue(user!!.id)
            val builder= AlertDialog.Builder(this)
            builder.apply{
                setTitle("Invitation")
                setMessage("You sent invitation")
                setNegativeButton("Undo"){_,_->
                    myRef.child(askedUser!!.name!!).child("askingUser").setValue("STOP")
                }
            }
            dialog=builder.create()
            try {
                dialog?.show()
            }
            catch (exception:Exception) {
            }

        }
        true
    }

    fun click(view: View) {
        finish()
    }
}