package com.example.to_do.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.to_do.R

class SpinnerCustomAdapter(val data: List<MyIcon>):BaseAdapter() {

    class ItemHolder(view: View){
        val name:TextView
        val image:ImageView

        init{
            name=view.findViewById(R.id.iconName)
            image=view.findViewById(R.id.iconPhoto)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view= LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item,parent,false)
        val viewHolder= ItemHolder(view)
        viewHolder.name.text=data[position].name
        //viewHolder.image.setImageResource(data[position].image)
        val url=when(position){
            0->"https://static.thenounproject.com/png/655186-200.png"
            1->"https://cdn.iconscout.com/icon/premium/png-512-thumb/easy-work-1659766-1409942.png"
            2->"https://icons-for-free.com/iconfiles/png/512/cart+ecommerce+shop+icon-1320166083122274571.png"
            3->"https://uxwing.com/wp-content/themes/uxwing/download/16-business-and-finance/meeting.png"
            4->"https://cdn.iconscout.com/icon/free/png-256/phone-call-16-1172958.png"
            else ->"https://static.thenounproject.com/png/2986724-200.png"
        }
        Glide.with(view)
            .load(url)
            .into(viewHolder.image);
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}