package com.example.to_do.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.Activities.ActivityAdd
import com.example.to_do.R
import com.example.to_do.Room.ItemDao
import com.example.to_do.Room.MyItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class CustomAdapter(private val data: MutableList<MyItem>, private val itemDao: ItemDao, private val click: RecyclerClick): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView
        val info: TextView
        val date: TextView
        val time: TextView
        val image: ImageView
        val v = view

        init {
            name = view.findViewById(R.id.name2)
            info = view.findViewById(R.id.information)
            date = view.findViewById(R.id.dateText)
            time = view.findViewById(R.id.timeText)
            image = view.findViewById(R.id.icon)
            view.setOnLongClickListener(){
                click.onLongClick(adapterPosition)
                true
            }
        }
    }

    fun removeItem(position: Int,viewHolder:RecyclerView.ViewHolder):MutableList<MyItem>{
        var state=true
        val item=data[position]
        data.removeAt(position)
        notifyItemRemoved(position)
        GlobalScope.launch {
            itemDao.delete(item)
        }
        Snackbar.make(viewHolder.itemView, item.name+" removed", Snackbar.LENGTH_LONG).setBackgroundTint(
            Color.BLACK)
            .setTextColor(Color.WHITE)
            .setAction("UNDO") {
                state=false
            data.add(position, item)
            notifyItemInserted(position)
            GlobalScope.launch {
                itemDao.insertAll(item)
            }
        }.show()
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image=listOf(R.drawable.friends, R.drawable.work, R.drawable.shopping, R.drawable.meeting, R.drawable.phone)
        val item=data[position]
        holder.name.text=item.name
        holder.info.text=item.info
        holder.date.text=item.date
        holder.time.text=item.time
        //holder.image.setImageResource(image[item.image!!])
        val url=when(item.image){
            0->"https://static.thenounproject.com/png/655186-200.png"
            1->"https://cdn.iconscout.com/icon/premium/png-512-thumb/easy-work-1659766-1409942.png"
            2->"https://icons-for-free.com/iconfiles/png/512/cart+ecommerce+shop+icon-1320166083122274571.png"
            3->"https://uxwing.com/wp-content/themes/uxwing/download/16-business-and-finance/meeting.png"
            4->"https://cdn.iconscout.com/icon/free/png-256/phone-call-16-1172958.png"
            else ->"https://static.thenounproject.com/png/2986724-200.png"
        }
        Glide.with(holder.itemView)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.image);
        if(item.important!!){
            holder.v.setBackgroundResource(R.drawable.greenpaper)
        }
        else{
            holder.v.setBackgroundResource(R.drawable.paper)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


}