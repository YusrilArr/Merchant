package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.gpay.merchantapp.R

class register1 : AppCompatActivity() {

//    private lateinit var btnnext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)


        val btnnext1 = findViewById<Button>(R.id.btn_next1)

        btnnext1.setOnClickListener {
            Intent(this, register2::class.java).also {
                startActivity(it)
            }
        }
    }


}