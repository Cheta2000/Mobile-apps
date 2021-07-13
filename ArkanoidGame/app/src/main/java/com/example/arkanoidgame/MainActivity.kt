package com.example.arkanoidgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var textRecord: TextView
    private lateinit var textInfo: TextView
    private lateinit var sharedPreferences: SharedPreferences
    var record=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences=getSharedPreferences("records", Context.MODE_PRIVATE)
        record=sharedPreferences.getInt("maxScore",0)
        textRecord=findViewById(R.id.textView)
        textRecord.text="RECORD: $record"
        textInfo=findViewById(R.id.textView2)
    }

    fun start(view: View) {
        val myIntent= Intent(this,GameActivity::class.java)
        startActivityForResult(myIntent,123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null) {
            val result = data.getIntExtra("result", 0)
            if(result==7) {
                textInfo.text ="YOU ARE THE BEST"
                textRecord.text="RECORD: Master"
            }
            else{
                textInfo.text="YOU LOST! SCORE: $result"
                if(result>record){
                    record=result
                    val editor=sharedPreferences.edit()
                    editor.putInt("maxScore",record)
                    editor.commit()
                    textRecord.text="RECORD: $record"
                }
            }
        }
        else{
            textInfo.text ="You left"
        }
    }
}