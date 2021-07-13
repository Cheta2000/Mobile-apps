package com.example.tictactoe

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.graphics.drawable.toDrawable
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

import kotlin.random.Random

class Game3 : AppCompatActivity() {

    private var roundCounter=0
    private var onMove=State.BLANK
    private var myMove=State.BLANK
    private var myName:String?=null
    private var hisName:String?=null
    private var buttons:ArrayList<Button> = arrayListOf()
    private lateinit var exitButton:Button
    private lateinit var toggle:ToggleButton
    private lateinit var move: TextView
    private lateinit var player1: TextView
    private lateinit var player2: TextView
    private var state=Array(3){Array(3){State.BLANK}}
    private val myIntent= Intent()
    private lateinit var myDatabase:FirebaseDatabase
    private lateinit var myRef:DatabaseReference


    enum class State{
        BLANK,X,O
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game3)
        player1=findViewById<TextView>(R.id.textView51)
        player2=findViewById<TextView>(R.id.textView52)
        move=findViewById<TextView>(R.id.textView53)
        val id=intent.getStringExtra("id")
        myName=intent.getStringExtra("user1")
        hisName=intent.getStringExtra("user2")
        if (id!![id.length - 1] == 'X') {
            myMove=State.X
            player1.setText("$myName: X")
            player2.setText("$hisName: O")
        }
        else{
            myMove=State.O
            player1.setText("$myName: O")
            player2.setText("$hisName: X")
        }
        if (id!![id.length - 2] == 'X') {
            onMove=State.X
            if(myMove==State.X) {
                move.text = "Your move(X)"
            }
            else{
                move.text="On move: $hisName(X)"
            }
        }
        else{
            onMove=State.O
            if(myMove==State.O) {
                move.text = "Your move(O)"
            }
            else{
                move.text="On move: $hisName(O)"
            }
        }
        myDatabase = Firebase.database
        myRef = myDatabase.getReference("3x3").child("games").child(id.substring(0,id.length-2))
        myRef.child("where").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val moveId=dataSnapshot.getValue<Integer>()
                if(moveId!=null) {
                    if(moveId.toInt()==-1){
                        playerLeft()
                    }
                    else {
                        move(moveId.toInt())
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        setup();
    }

    private fun playerLeft() {
        for(i in 0 until 8){
            buttons[i].isEnabled = false
        }
        move.text="$hisName left. You win!"
    }


    private fun setup() {
        for(i in 1 .. 9){
            buttons.add(findViewById<Button>(resources.getIdentifier("button3"+i,"id",packageName)))
        }
        exitButton=findViewById<Button>(R.id.button526)
        toggle = findViewById<ToggleButton>(R.id.toggleButton)
        myIntent.putExtra("winner",getString(R.string.NoRes))
        setResult(Activity.RESULT_OK,myIntent)
    }

    fun click(view: View) {
        var i=0
        val clicked=findViewById<Button>(view.id)
        if(exitButton==clicked){
            myRef.child("where").setValue(-1)
            finish();
        }
        if(toggle==clicked){
            val layout=findViewById<View>(R.id.main)
            if(toggle.isChecked) {
                layout.setBackgroundResource(R.color.dark_green)
            }
            else{
                layout.setBackgroundResource(R.color.green)
            }
        }
        if(onMove==myMove) {
            while (i < 9) {
                if (buttons[i] == clicked) {
                    myRef.child("where").setValue(i)
                    break
                } else {
                    i++;
                }
            }
        }
    }

    private fun move(i:Int){
        roundCounter++;
        if (onMove == State.O) {
            state[i / 3][i % 3] = State.O;
            buttons[i].text = getString(R.string.Oval)
            if (checkResult(i / 3, i % 3, onMove)) {
                if(myMove==State.X) {
                    move.text = "Your move(X)"
                }
                else{
                    move.text="On move: $hisName(X)"
                }
                onMove = State.X
            }
        } else {
            state[i / 3][i % 3] = State.X;
            buttons[i].text = getString(R.string.Cross)
            if (checkResult(i / 3, i % 3, onMove)) {
                if(myMove==State.O) {
                    move.text = "Your move(O)"
                }
                else{
                    move.text="On move: $hisName(O)"
                }
                onMove = State.O
            }
        }
        buttons[i].isEnabled = false
    }

    private fun checkResult(y:Int,x:Int,who:State):Boolean {
        if(roundCounter<5){
            return true
        }
        else{
            for(i in 0..2){
                if(state[y][i]!=who){
                    break
                }
                else if(i==2){
                    for(j in 0..2){
                        buttons[y*3+j].setBackgroundResource(R.drawable.win_button)
                    }
                    end(who)
                    return false
                }
            }
            for(i in 0..2){
                if(state[i][x]!=who){
                    break
                }
                else if(i==2){
                    for(j in 0..2) {
                        buttons[x + 3 * j].setBackgroundResource(R.drawable.win_button)
                    }
                    end(who)
                    return false
                }
            }
            if(x==y) {
                for (i in 0..2) {
                    if(state[i][i]!=who){
                        break
                    }
                    else if(i==2){
                        for(j in 0..2) {
                            buttons[j*4].setBackgroundResource(R.drawable.win_button)
                        }
                        end(who)
                        return false
                    }
                }
            }
            if(y==2-x){
                for(i in 0..2){
                    if(state[2-i][i]!=who){
                        break
                    }
                    else if(i==2){
                        for (j in 0..2){
                            buttons[2+2*j].setBackgroundResource(R.drawable.win_button)
                        }
                        end(who)
                        return false
                    }
                }
            }
            if(roundCounter==9){
                end(State.BLANK)
                return false
            }
            return true
        }

    }

    private fun end(who:State){
        for(i in 0 until 9){
            buttons[i].isEnabled=false
        }
        if(who==State.BLANK) {
            move.text = getString(R.string.Deuce)
            myIntent.putExtra("winner",getString(R.string.Deuce))
        }
        else if(who==State.O){
            if(myMove==State.O) {
                move.text = "You win!";
            }
            else{
                move.text="$hisName win!"
            }
            myIntent.putExtra("winner",getString(R.string.WinOval))
        }
        else {
            if(myMove==State.X) {
                move.text = "You win!";
            }
            else{
                move.text="$hisName win!"
            }
            myIntent.putExtra("winner", getString(R.string.WinCross))
        }
        setResult(Activity.RESULT_OK,myIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        myRef.setValue(null)
    }
}