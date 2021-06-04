package com.example.busnusantara

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.example.busnusantara.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.TypeVariable


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnSearchRoute.setOnClickListener {
            val startLocation = etStartLocation.text.toString()
            val destination = etDestination.text.toString()

            if (startLocation.isNotEmpty() && destination.isNotEmpty()) {
                Firebase.firestore.collection(Collections.ROUTES.toString())
                    .whereEqualTo("destination", destination)
                    .get().addOnSuccessListener { documents ->
                        Log.d(ContentValues.TAG, "Finding all routes with $destination dest")
                        if (documents.isEmpty) {
                            textView.text = "No route was found. Please try again"
                        }
                        for (document in documents) {
                            val stopsData = document.data.get("stops")
                            if (stopsData is List<*>) {
                                var stops: List<String> = stopsData.filterIsInstance<String>()
                                stops = listOf(startLocation) + stops + destination
                                val stopsAdapter =
                                    ArrayAdapter(this, android.R.layout.simple_list_item_1, stops)
                                stopsList.setAdapter(stopsAdapter)
                                textView.text = "Route found."
                                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                            }
                        }
                    }
            }
        }
    }
}