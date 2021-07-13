package com.example.tictactoe

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase


class CustomAdapter(private val data:List<User>,private val myRef:DatabaseReference,private val click: RecyclerClick):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textView:TextView
        val button:Button
        init{
            textView=view.findViewById(R.id.textView);
            button=view.findViewById(R.id.button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate((R.layout.user_item),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=data[position]
        holder.textView.text=user.name
        holder.button.setOnClickListener{
            click.onClick(position)
        }
    }



    override fun getItemCount(): Int {
        return data.size;
    }
}