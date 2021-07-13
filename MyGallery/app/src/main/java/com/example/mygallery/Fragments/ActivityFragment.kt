package com.example.mygallery.Fragments

import android.app.Activity.RESULT_OK
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mygallery.Recycler.MyImage
import com.example.mygallery.R

class ActivityFragment : Fragment() {

    val descriptions= arrayListOf("Szczęśliwy, biały pies","Psy biegające w trawie","Mały pies na spacerze","Kot samotnik","Wypoczywający na krześle kotek","Kot brytyjski","Papużki nierozłączki","Powolny żółw","Ogromny słoń","Delfin czekający na posiłek","Zółty kwiat","Piękny rozłożysty kwiat","Krzew z kwiatkami","Groźna roślina","Rośliny domowe","Czy znasz te kwiaty?","Para","Dzieci","Przyjaźń w Rosji","Studenci po sesji","Jezioro pośród gór","Zachód słońca nad miastem","Zimowy krajobraz")
    lateinit var textView:TextView
    lateinit var imageView: ImageView
    lateinit var ratingBar:RatingBar
    lateinit var button: Button
    var rate=0.0F

    override fun onStop() {
        super.onStop()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            activity!!.finish()
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val image=activity!!.intent.getIntExtra("index",0)
        rate=activity!!.intent.getFloatExtra("rate",0.0F)
        if(image==0){
            view!!.visibility=View.INVISIBLE
        }
        else {
            textView.text = descriptions[image - 1]
            ratingBar.visibility=View.VISIBLE
            ratingBar.rating=rate
        }
        imageView.setImageResource(resources.getIdentifier(("pic"+image.toString()),"drawable",view!!.context.packageName))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_activity, container, false)
        val recycler=view.findViewById<ViewPager2>(R.id.view)
        textView=view.findViewById(R.id.textView)
        imageView=view.findViewById(R.id.imageView)
        ratingBar=view.findViewById(R.id.ratingBar)
        button=view.findViewById(R.id.button)
        ratingBar=view.findViewById(R.id.ratingBar)
        button.setOnClickListener {
            activity!!.intent.putExtra("rate",ratingBar.rating)
            activity!!.setResult(RESULT_OK,activity!!.intent)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity!!.finish()
            } else {
                view.visibility=View.INVISIBLE
                val frag=fragmentManager!!.findFragmentById(R.id.mainFrag) as MainFragment
                frag.rated(ratingBar.rating)
            }
        }
        return view
    }

    fun display(item: MyImage){
        view!!.visibility=View.VISIBLE
        textView.text=descriptions[item.image-1]
        imageView.setImageResource(resources.getIdentifier(("pic"+item.image.toString()),"drawable",view!!.context.packageName))
        imageView.layoutParams.height=500
        imageView.layoutParams.width=500
        ratingBar.rating=item.rate
    }
}