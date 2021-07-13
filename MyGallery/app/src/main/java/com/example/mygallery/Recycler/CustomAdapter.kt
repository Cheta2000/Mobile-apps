package com.example.mygallery.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.R
import com.example.to_do.Adapters.RecyclerClick

class CustomAdapter(private val data:List<MyImage>, private val click: RecyclerClick):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView:ImageView
        init{
            imageView=view.findViewById(R.id.itemImageView);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate((R.layout.item),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setOnClickListener{
           click.onClick(position)
        }
        holder.imageView.setImageResource(holder.itemView.resources.getIdentifier(("pic"+data[position].image.toString()),"drawable",holder.itemView.context.packageName))
    }

    override fun getItemCount(): Int {
        return data.size;
    }
}