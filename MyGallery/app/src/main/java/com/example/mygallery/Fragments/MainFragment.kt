package com.example.mygallery.Fragments

import android.app.ActionBar
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.Activities.ImageActivity
import com.example.mygallery.Recycler.CustomAdapter
import com.example.mygallery.Recycler.MyImage
import com.example.mygallery.R
import com.example.to_do.Adapters.RecyclerClick

class MainFragment : Fragment(),RecyclerClick {

    var items= arrayListOf<MyImage>()
    lateinit var adapter: CustomAdapter
    lateinit var recyclerView:RecyclerView
    var index=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_main, container, false)
        if(savedInstanceState!=null){
            val restore=savedInstanceState.getParcelableArrayList<MyImage>("data")
            items=restore!!
        }
        else {
            for (i in 1..23) {
                val image = MyImage(i, 0.0F)
                items.add(image)
            }
        }
        recyclerView=view.findViewById(R.id.recyclerView)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager=GridLayoutManager(view.context,3)
        } else {
            recyclerView.layoutManager=GridLayoutManager(view.context,6)

        }
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(DividerItemDecoration(view.context,
            DividerItemDecoration.HORIZONTAL))
        recyclerView.addItemDecoration(DividerItemDecoration(view.context,
            DividerItemDecoration.VERTICAL))
        adapter= CustomAdapter(items, this)
        recyclerView.adapter=adapter
        items.sortByDescending { it.rate }
        adapter.notifyDataSetChanged()
        return view;
    }

    override fun onClick(position: Int) {
        index=position
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val myIntent= Intent(view!!.context,
                ImageActivity::class.java)
            myIntent.putExtra("index",items[index].image)
            myIntent.putExtra("rate",items[index].rate)
            startActivityForResult(myIntent,123)
        } else {
            if(recyclerView.layoutParams.width!=800) {
                recyclerView.layoutManager = GridLayoutManager(view!!.context, 3)
                recyclerView.layoutParams.width = 800
                recyclerView.scrollToPosition(position)
            }
            val frag=fragmentManager!!.findFragmentById(R.id.activityFrag) as ActivityFragment
            frag.display(items[index])
        }
    }

    fun rated(rate:Float){
        recyclerView.layoutManager=GridLayoutManager(view!!.context,6)
        recyclerView.layoutParams.width=ActionBar.LayoutParams.WRAP_CONTENT
        items[index].rate=rate
        items.sortByDescending { it.rate }
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if(data!=null) {
                val rate = data.getFloatExtra("rate", 0.0F)
                items[index].rate = rate
                items.sortByDescending { it.rate }
                adapter.notifyDataSetChanged()
            }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("data",items)
    }
}

