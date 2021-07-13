package com.example.tictactoe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class Game5 : AppCompatActivity() {

    private var roundCounter=0
    private var onMove=State.BLANK
    private val buttons:ArrayList<Button> = arrayListOf()
    private lateinit var exitButton:Button
    private lateinit var move: TextView
    private var state=Array(5){Array(5){State.BLANK}}
    private val myIntent= Intent()

    enum class State{
        BLANK,X,O
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game5)
        setup();
    }

    private fun setup() {
        for(i in 1 .. 25){
            buttons.add(findViewById<Button>(resources.getIdentifier("button5"+i,"id",packageName)))
        }
        exitButton=findViewById<Button>(R.id.button526)
        move=findViewById<TextView>(R.id.textView53)
        val start = Random.nextInt(10)
        if (start < 5) {
            onMove=State.O
            move.text=getString(R.string.Player1)
        } else {
            move.text=getString(R.string.Player2)
            onMove=State.X
        }
        myIntent.putExtra("winner",getString(R.string.NoRes))
        setResult(Activity.RESULT_OK,myIntent)
    }

    fun click(view: View) {
        var i=0
        val clicked=findViewById<Button>(view.id)
        if(exitButton==clicked){
            finish();
        }
        roundCounter++;
        while(i<25){
            if(buttons[i]==clicked){
                if(onMove==State.O) {
                    state[i/5][i%5]=State.O;
                    buttons[i].text = getString(R.string.Oval)
                    if(checkResult(i/5,i%5,onMove)) {
                        move.text = getString((R.string.Player2))
                        onMove = State.X
                    }
                }
                else{
                    state[i/5][i%5]=State.X;
                    buttons[i].text=getString(R.string.Cross)
                    if(checkResult(i/5,i%5,onMove)) {
                        move.text = getString(R.string.Player1)
                        onMove = State.O
                    }
                }
                buttons[i].isEnabled=false
                break;
            }
            else {
                i++;
            }
        }
    }

    private fun checkResult(y:Int,x:Int,who:State):Boolean {
        if(roundCounter<9){
            return true
        }
        else{
            for(i in 0..4){
                if(state[y][i]!=who){
                    break
                }
                else if(i==4){
                    for(j in 0..4){
                        buttons[y*5+j].setBackgroundResource(R.drawable.win_button)
                    }
                    end(who)
                    return false
                }
            }
            for(i in 0..4){
                if(state[i][x]!=who){
                    break
                }
                else if(i==4){
                    for(j in 0..4) {
                        buttons[x + 5 * j].setBackgroundResource(R.drawable.win_button)
                    }
                    end(who)
                    return false
                }
            }
            if(x==y) {
                for (i in 0..4) {
                    if(state[i][i]!=who){
                        break
                    }
                    else if(i==4){
                        for(j in 0..4) {
                            buttons[j*6].setBackgroundResource(R.drawable.win_button)
                        }
                        end(who)
                        return false
                    }
                }
            }
            if(y==4-x){
                for(i in 0..4){
                    if(state[4-i][i]!=who){
                        break
                    }
                    else if(i==4){
                        for (j in 0..4){
                            buttons[4+4*j].setBackgroundResource(R.drawable.win_button)
                        }
                        end(who)
                        return false
                    }
                }
            }
            if(roundCounter==25){
                end(State.BLANK)
                return false
            }
            return true
        }

    }

    private fun end(who:State){
        for(i in 0 until 25){
            buttons[i].isEnabled=false
        }
        if(who==State.BLANK) {
            move.text = getString(R.string.Deuce)
            myIntent.putExtra("winner",getString(R.string.Deuce))
        }
        else if(who==State.O){
            move.text = getString(R.string.EndO);
            myIntent.putExtra("winner",getString(R.string.WinOval))
        }
        else {
            move.text = getString(R.string.EndX)
            myIntent.putExtra("winner", getString(R.string.WinCross))
        }
        setResult(Activity.RESULT_OK,myIntent)
    }
}