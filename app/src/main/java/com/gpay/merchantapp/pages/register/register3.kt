package com.gpay.merchantapp.pages.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister3Binding

class register3 : AppCompatActivity() {
    private lateinit var binding : ActivityRegister3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backReg3.setOnClickListener {
            finish()
        }
    }
}