package com.nakharin.mapfarmer.Controller.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import com.nakharin.mapfarmer.R

class MainActivity : AppCompatActivity() {

    private lateinit var btnMap: AppCompatButton
    private lateinit var btnSCard: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidget()

        btnMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        btnSCard.setOnClickListener {
            val intent = Intent(this, IdentiveGetZActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initWidget() {
        btnMap = findViewById(R.id.btnMap)
        btnSCard = findViewById(R.id.btnSCard)
    }
}