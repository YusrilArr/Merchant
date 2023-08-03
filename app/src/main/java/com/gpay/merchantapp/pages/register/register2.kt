package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister2Binding

class register2 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backReg2.setOnClickListener {
            finish()
        }

        val btnnext2 = findViewById<Button>(R.id.btn_next2)
        btnnext2.setOnClickListener {
            Intent(this, register3::class.java).also {
                startActivity(it)
            }
        }
    }
}