package com.example.hangman

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var word=""
    private var lifes=12
    private var words: Array<String> = arrayOf()
    private lateinit var right: IntArray
    private lateinit var textWord: TextView
    private lateinit var input: TextView
    private lateinit var info: TextView
    private lateinit var confirm: Button
    private lateinit var delete: Button
    private lateinit var reset: Button
    private lateinit var end: Button
    private var buttons:ArrayList<Button> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        words=resources.getStringArray(R.array.words)
        textWord=findViewById<TextView>(R.id.textView1)
        input=findViewById<TextView>(R.id.textView3)
        info=findViewById<TextView>(R.id.textView2)
        confirm=findViewById<Button>(R.id.buttonConf)
        delete=findViewById<Button>(R.id.buttonDel)
        reset=findViewById<Button>(R.id.buttonReset)
        end=findViewById<Button>(R.id.buttonEnd)
        for(i in 1 .. 26){
            buttons.add(findViewById<Button>(resources.getIdentifier("button"+i,"id",packageName)))
        }
        buttons[0].setOnLongClickListener(){
            input.append(" ")
            true
        }
        buttons[2].setOnLongClickListener(){
            input.append("Ę")
            true
        }
        buttons[8].setOnLongClickListener(){
            input.append("Ó")
            true
        }
        buttons[10].setOnLongClickListener(){
            input.append("Ą")
            true
        }
        buttons[11].setOnLongClickListener(){
            input.append("Ś")
            true
        }
        buttons[18].setOnLongClickListener(){
            input.append("Ł")
            true
        }
        buttons[19].setOnLongClickListener(){
            input.append("Ż")
            true
        }
        buttons[20].setOnLongClickListener(){
            input.append("Ź")
            true
        }
        buttons[21].setOnLongClickListener(){
            input.append("Ć")
            true
        }
        buttons[24].setOnLongClickListener(){
            input.append("Ń")
            true
        }
        delete.setOnLongClickListener(){
            input.text=""
            true
        }
        setup()
    }

    private fun setup() {
        word=words[Random.nextInt(words.size)]
        right= IntArray(word.length){0}
        textWord.text=""
        for(i in 0 until word.length){
            if(word[i].isWhitespace()){
                textWord.append("\n")
                right[i]=2
            }
            else {
                textWord.append("__ ")
            }
        }
    }

    fun click(view: View) {
        var ok=0
        val check = input.text.toString()
        val clicked=findViewById<Button>(view.id)
        if(clicked==confirm) {
            input.text=""
            if (check.length > 1) {
                if (check.equals(word, true)) {
                    end()
                }
                else {
                    bad()
                }
            } else {
                for (i in 0 until word.length) {
                    if (check.equals(word[i].toString(), true)) {
                        Toast.makeText(this,getString(R.string.Good),Toast.LENGTH_SHORT).show()
                        right[i] = 1
                        ok++
                    }
                }
                if (ok == 0) {
                    bad()
                } else {
                    set()
                }
            }
        }
        else if(clicked==delete){
            var sub=input.text
            if(sub.length>0) {
                sub = sub.substring(0, sub.length - 1)
                input.text = sub
            }
        }
        else if(clicked==reset){
            restart()
        }
        else if(clicked==end){
            finish()
        }
        else{
            val sign=clicked.text
            input.append(sign)
        }
    }

    private fun restart() {
        val image=findViewById<ImageView>(R.id.imageView)
        image.setImageResource(0)
        for(i in 0 until 26){
            buttons[i].isEnabled=true
        }
        confirm.isEnabled=true
        delete.isEnabled=true
        lifes=12
        input.text=""
        info.setTextColor(Color.BLACK)
        info.text=getString(R.string.Ask)
        setup()
    }

    fun end(){
        info.setTextColor(Color.RED)
        textWord.text=word
        info.text=""
        input.text=getString(R.string.Win)
        disableButtons()
    }

    fun set(){
        var end=true
        textWord.text=""
        for (i in 0 until right.size){
            if(right[i]==1){
                textWord.append(word[i].toString()+" ")
            }
            else if(right[i]==2){
                textWord.append("\n")
            }
            else{
                end=false
                textWord.append("__ ")
            }
        }
        if(end){
            end()
        }
    }

    fun bad(){
        Toast.makeText(this,getString(R.string.Bad),Toast.LENGTH_SHORT).show()
        lifes--
        val image=findViewById<ImageView>(R.id.imageView)
        image.setImageResource(resources.getIdentifier("hang"+(12-lifes).toString(),"drawable",packageName))
        if(lifes==1){
            info.setTextColor(Color.RED)
        }
        if(lifes==0){
            info.setTextColor(Color.BLACK)
            textWord.text=word
            info.text=""
            input.text=getString(R.string.Lose)
            disableButtons()
        }
    }

    fun disableButtons(){
        for(i in 0 until 26){
            buttons[i].isEnabled=false
        }
        confirm.isEnabled=false
        delete.isEnabled=false
    }
}