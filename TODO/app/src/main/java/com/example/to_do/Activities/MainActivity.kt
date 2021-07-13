package com.example.to_do.Activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.*
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.to_do.Adapters.CustomAdapter
import com.example.to_do.Adapters.RecyclerClick
import com.example.to_do.Adapters.SwipeToDelete
import com.example.to_do.R
import com.example.to_do.Room.AppDatabase
import com.example.to_do.Room.ItemDao
import com.example.to_do.Room.MyItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import java.text.FieldPosition


class MainActivity : AppCompatActivity(),RecyclerClick {

    private val CHANNEL_ID="channel_id_1"
    private var items=mutableListOf<MyItem>()
    private lateinit var adapter: CustomAdapter
    private lateinit var spinner: Spinner
    private lateinit var recycler:RecyclerView
    private lateinit var itemDao: ItemDao
    private var changeID=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, getString(R.string.dbName)
        ).build()
        itemDao=db.itemDao()
        startNotify()
        recycler=findViewById(R.id.recyclerView)
        adapter= CustomAdapter(items,itemDao,this)
        if(resources.configuration.orientation== Configuration.ORIENTATION_PORTRAIT) {
            recycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val swiper=object:SwipeToDelete(this,0,ItemTouchHelper.UP){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    items=adapter.removeItem(viewHolder.adapterPosition,viewHolder)
                }
            }
            val itemTouchHelper=ItemTouchHelper(swiper)
            itemTouchHelper.attachToRecyclerView(recycler)
        }
        else{
            recycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val swiper=object:SwipeToDelete(this,0,ItemTouchHelper.LEFT){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    items=adapter.removeItem(viewHolder.adapterPosition,viewHolder)
                }
            }
            val itemTouchHelper=ItemTouchHelper(swiper)
            itemTouchHelper.attachToRecyclerView(recycler)
        }
        val helper: SnapHelper = LinearSnapHelper()
        helper.attachToRecyclerView(recycler)
        spinner=findViewById(R.id.spinner2)
        val arrayAdapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.sortStyle))
        spinner.adapter=arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->items.sortBy{it.id}
                    1->items.sortWith(compareBy({ it.date }, { it.time }))
                    2->items.sortBy{it.image}
                    3->items.sortByDescending{it.important}
                }
                adapter= CustomAdapter(items, itemDao,this@MainActivity)
                recycler.adapter=adapter
            }
        }
        if(savedInstanceState!=null){
            val l1=savedInstanceState.getStringArrayList("1")
            val l2=savedInstanceState.getIntegerArrayList("2")
            val l3=savedInstanceState.getStringArrayList("3")
            val l4=savedInstanceState.getStringArrayList("4")
            val l5=savedInstanceState.getStringArrayList("5")
            val l6=savedInstanceState.getIntegerArrayList("6")
            val l7=savedInstanceState.getIntegerArrayList("7")
            for(i in 0 until l1!!.size){
                val newItem: MyItem = if(l6!![i]==1) {
                    MyItem(l7!![i], l1[i], l2!![i], l3!![i], l4!![i], l5!![i], true)
                } else{
                    MyItem(l7!![i], l1[i], l2!![i], l3!![i], l4!![i], l5!![i], false)
                }
                items.add(newItem)
            }
            adapter.notifyDataSetChanged()
        }
        else{
            refreshItems()
        }
        findViewById<Button>(R.id.button).setOnClickListener{click(it)}
    }

    override fun onLongClick(position: Int) {
        val item=items[position]
        changeID=item.id!!
        val myIntent=Intent(this,ActivityAdd::class.java)
        myIntent.putExtra("name",item.name)
        myIntent.putExtra("info",item.info)
        myIntent.putExtra("date",item.date)
        myIntent.putExtra("time",item.time)
        myIntent.putExtra("important",item.important)
        myIntent.putExtra("icon",item.image)
        startActivityForResult(myIntent,124)
    }

    fun click(view: View) {
        val myIntent= Intent(this, ActivityAdd::class.java)
        startActivityForResult(myIntent,123)
    }

    private fun startNotify(){
        GlobalScope.launch {
            while (true) {
                val items = itemDao.toNotify();
                for (i in items) {
                    sendNotification(i.name + " at: " + i.date + " " + i.time,i.id!!);
                }
                delay(10800000)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val l1= arrayListOf<String>()
        val l2= arrayListOf<Int>()
        val l3= arrayListOf<String>()
        val l4= arrayListOf<String>()
        val l5= arrayListOf<String>()
        val l6= arrayListOf<Int>()
        val l7= arrayListOf<Int>()
        for(i in 0 until items.size){
            l1.add(items[i].name!!)
            l2.add(items[i].image!!)
            l3.add(items[i].date!!)
            l4.add(items[i].time!!)
            l5.add(items[i].info!!)
            l7.add(items[i].id!!)
            if(items[i].important!!){
                l6.add(1)
            }
            else{
                l6.add(0)
            }
        }
        outState.putStringArrayList("1",l1)
        outState.putIntegerArrayList("2",l2)
        outState.putStringArrayList("3",l3)
        outState.putStringArrayList("4",l4)
        outState.putStringArrayList("5",l5)
        outState.putIntegerArrayList("6",l6)
        outState.putIntegerArrayList("7",l7)
        super.onSaveInstanceState(outState)
    }

    private fun refreshItems(){
        GlobalScope.launch {
            items = itemDao.getAll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            val info=data?.getStringExtra("info")
            val image=data?.getIntExtra("image",0)
            val date=data?.getStringExtra("date")
            val time=data?.getStringExtra("time")
            val name=data?.getStringExtra("name")
            val important=data?.getBooleanExtra("important",false)
            if(name!=null) {
                if(requestCode==123) {
                    MotionToast.createColorToast(this,getString(R.string.success),getString(R.string.successText),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.montserrat_regular))
                    var max=-1
                    for(i in 0 until items.size){
                        if(max<items[i].id!!){
                            max=items[i].id!!
                        }
                    }
                    val newItem = MyItem(max+1, name, image, date, time, info, important)
                    GlobalScope.launch {
                       itemDao.insertAll(newItem)
                    }
                    items.add(newItem)
                    adapter.notifyItemInserted(items.size-1)
                }
                if(requestCode==124) {
                    MotionToast.createColorToast(this,getString(R.string.change),getString(R.string.changeText),
                        MotionToast.TOAST_INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.montserrat_regular))
                    val newItem = MyItem(changeID,name,image,date,time,info,important)
                    GlobalScope.launch {
                        itemDao.deleteByID(changeID)
                        itemDao.insertAll(newItem)
                    }
                    for (i in 0 until items.size){
                        if(items[i].id==changeID){
                            items[i]=newItem
                            break
                        }
                    }
                }
                when(spinner.selectedItemPosition){
                    0->items.sortBy{it.id}
                    1->items.sortWith(compareBy({ it.date }, { it.time }))
                    2->items.sortBy{it.image}
                    3->items.sortByDescending{it.important}
                }
                adapter.notifyDataSetChanged()
            }
        }



    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name="Channel"
            val text="ChannelText"
            val importance= NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=text;
            }
            val notificationManager: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(info:String,id:Int) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.time)
                .setContentTitle(getString(R.string.reminder))
                .setContentText(info)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)) {
            notify(id, builder.build())
        }
    }

}