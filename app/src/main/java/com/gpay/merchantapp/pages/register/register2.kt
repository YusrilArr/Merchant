package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.gpay.merchantapp.R

class register2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        val btnnext2 = findViewById<Button>(R.id.btn_next2)
        btnnext2.setOnClickListener {
            Intent(this, register3::class.java).also {
                startActivity(it)
            }
        }
    }
}