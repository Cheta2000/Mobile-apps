package com.example.to_do.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.example.to_do.Adapters.MyIcon
import com.example.to_do.Adapters.SpinnerCustomAdapter
import com.example.to_do.R
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.*

class ActivityAdd : AppCompatActivity() {

    private var items=listOf<MyIcon>()
    private lateinit var spinner:Spinner
    private lateinit var textTime:TextView
    private lateinit var textDate:TextView
    private lateinit var name:TextView
    private lateinit var info:TextView
    private lateinit var checkBox:CheckBox


    private fun generate(): List<MyIcon> {
        val new1= MyIcon(getString(R.string.friends), R.drawable.friends)
        val new2= MyIcon(getString(R.string.work), R.drawable.work)
        val new3= MyIcon(getString(R.string.shop), R.drawable.shopping)
        val new4= MyIcon(getString(R.string.meet), R.drawable.meeting)
        val new5= MyIcon(getString(R.string.call), R.drawable.phone)
        return listOf(new1,new2,new3,new4,new5)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        name=findViewById<TextView>(R.id.name2)
        info=findViewById<TextView>(R.id.info)
        textTime=findViewById(R.id.textTime)
        textDate=findViewById(R.id.textDate)
        checkBox=findViewById<CheckBox>(R.id.important)
        spinner=findViewById<Spinner>(R.id.spinner)
        items=generate()
        val adapter= SpinnerCustomAdapter(items)
        spinner.adapter=adapter
        if(savedInstanceState!=null){
            val d=savedInstanceState.getString("date")
            val t=savedInstanceState.getString("time")
            textTime.text=t
            textDate.text=d
        }
        if(intent!=null){
            val nameInt=intent.getStringExtra("name")
            val infoInt=intent.getStringExtra("info")
            val dateInt=intent.getStringExtra("date")
            val timeInt=intent.getStringExtra("time")
            val importantInt=intent.getBooleanExtra("important",false)
            val icon=intent.getIntExtra("icon",0)
            name.text=nameInt
            info.text=infoInt
            textTime.text=timeInt
            textDate.text=dateInt
            if(importantInt){
                checkBox.isChecked=true
            }
            spinner.setSelection(icon)
        }
        findViewById<Button>(R.id.buttonDate).setOnClickListener{datePicker(it)}
        findViewById<Button>(R.id.buttonTime).setOnClickListener{timePicker(it)}
        findViewById<Button>(R.id.buttonConfirm).setOnClickListener{confirm(it)}
        findViewById<Button>(R.id.exit).setOnClickListener{exit(it)}
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("date",textDate.text.toString())
        outState.putString("time",textTime.text.toString())
    }


    fun timePicker(view: View) {
        val cal= Calendar.getInstance()
        val timeSetListener= TimePickerDialog.OnTimeSetListener{ view, hour, minute->
            cal.set(Calendar.HOUR_OF_DAY,hour)
            cal.set(Calendar.MINUTE,minute)
            textTime.text=SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }
    fun datePicker(view: View) {
        val cal= Calendar.getInstance()
        val dateSetListener= DatePickerDialog.OnDateSetListener{ view, year, month, day ->
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,day)
            textDate.text=SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        }
        DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }

    fun exit(view: View){
        finish()
    }

    fun confirm(view: View){
        val myIntent= Intent()
        if(name.text.isEmpty()){
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyName),
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return
        }
        myIntent.putExtra("name",name.text.toString())
        myIntent.putExtra("info", info.text.toString())
        if(textDate.text.isEmpty()){
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyDate),
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return
        }
        myIntent.putExtra("date",textDate.text)
        if(textTime.text.isEmpty()){
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyTime),
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return
        }
        myIntent.putExtra("time",textTime.text)
        val choice=spinner.selectedItem as MyIcon
        val num=when(choice.name){
            getString(R.string.friends)->0
            getString(R.string.work)->1
            getString(R.string.shop)->2
            getString(R.string.meet)->3
            getString(R.string.call)->4
            else->0
        }
        myIntent.putExtra("image",num)
        if(checkBox.isChecked){
            myIntent.putExtra("important",true)
        }
        val builder= AlertDialog.Builder(view.context)
        builder.setTitle(getString(R.string.task))
        builder.setMessage(""+name.text+"\n"+info.text+"\n"+textDate.text+" "+textTime.text)
        builder.setPositiveButton(view.context.getString(R.string.yes)){ _, _->
            setResult(AppCompatActivity.RESULT_OK,myIntent)
            finish()
        }
        builder.setNegativeButton(view.context.getString(R.string.no)){ _, _->}
        builder.setIcon(R.drawable.ask)
        val dialog=builder.create()
        dialog.show()
    }
}


