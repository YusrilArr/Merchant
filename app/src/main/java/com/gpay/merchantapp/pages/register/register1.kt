package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister1Binding
import com.gpay.merchantapp.databinding.ActivityRegister2Binding

class register1 : AppCompatActivity() {

//    private lateinit var btnnext: Button

    private lateinit var binding : ActivityRegister1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backReg1.setOnClickListener {
            finish()
        }


        val btnnext1 = findViewById<Button>(R.id.btn_next1)

        btnnext1.setOnClickListener {
            Intent(this, register2::class.java).also {
                startActivity(it)
            }
        }
    }


}