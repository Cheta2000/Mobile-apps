package com.example.citymap

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.*
import java.time.Duration


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var spinner: Spinner
    private var cities = mutableMapOf<String, HashMap<String, Double>>()
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                reload()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("cities")
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cities = dataSnapshot.value as MutableMap<String, HashMap<String, Double>>
                val keys = ArrayList(cities.keys)
                val adapter = ArrayAdapter<String>(
                    this@MapsActivity,
                    R.layout.support_simple_spinner_dropdown_item,
                    keys
                )
                spinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        myRef.addValueEventListener(postListener)
    }

    private fun reload() {
        val place =
            LatLng(cities[spinner.selectedItem]!!["x"]!!, cities[spinner.selectedItem]!!["y"]!!)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(place).title(spinner.selectedItem.toString()))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place))

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    fun add(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add city")
        val layout = LinearLayout(this)
        val name = EditText(this)
        name.inputType = InputType.TYPE_CLASS_TEXT
        name.setHint("Name")
        val x = EditText(this)
        x.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        x.setHint("x")
        val y = EditText(this)
        y.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        y.setHint("y")
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(name)
        layout.addView(x)
        layout.addView(y)
        builder.setView(layout)
        builder.setPositiveButton(
            "CONFIRM"
        ) { _, _ ->
            myRef.child(name.text.toString()).child("x").setValue(x.text.toString().toDouble())
            myRef.child(name.text.toString()).child("y").setValue(y.text.toString().toDouble())
            Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(
            "CANCEL"
        ) { _, _ -> }
        builder.show()
    }

    fun draw(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Draw line")
        val layout = LinearLayout(this)
        val name1 = EditText(this)
        name1.inputType = InputType.TYPE_CLASS_TEXT
        name1.setHint("City 1")
        val name2 = EditText(this)
        name2.inputType = InputType.TYPE_CLASS_TEXT
        name2.setHint("City 2")
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(name1)
        layout.addView(name2)
        builder.setView(layout)
        builder.setPositiveButton(
            "CONFIRM"
        ) { _, _ ->
            if (cities.containsKey(name1.text.toString())) {
                if (cities.containsKey(name2.text.toString())) {
                    val line: Polyline = mMap.addPolyline(
                        PolylineOptions()
                            .add(LatLng(cities[name1.text.toString()]!!["x"]!!,cities[name1.text.toString()]!!["y"]!!), LatLng(cities[name2.text.toString()]!!["x"]!!,cities[name2.text.toString()]!!["y"]!!))
                            .width(5f)
                            .color(Color.RED)
                    )
                    mMap.addMarker(MarkerOptions().position(LatLng(cities[name1.text.toString()]!!["x"]!!,cities[name1.text.toString()]!!["y"]!!)).title(name1.toString()))
                    mMap.addMarker(MarkerOptions().position(LatLng(cities[name2.text.toString()]!!["x"]!!,cities[name2.text.toString()]!!["y"]!!)).title(name2.toString()))
                }
                else {
                    Toast.makeText(this, "Unknown city ${name2.text.toString()}!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Unknown city ${name1.text.toString()}!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(
            "CANCEL"
        ) { _, _ -> }
        builder.show()
    }

    fun clear(view: View) {
        mMap.clear()
    }

}