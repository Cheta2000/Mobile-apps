package com.example.to_do.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.R

abstract class SwipeToDelete(context: Context,dragDirection: Int,swipeDirection: Int):ItemTouchHelper.SimpleCallback(dragDirection,swipeDirection){
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }
}