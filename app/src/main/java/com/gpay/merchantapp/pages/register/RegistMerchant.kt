package com.gpay.merchantapp.pages.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.gpay.merchantapp.databinding.ActivityRegistMerchantBinding

class RegistMerchant : AppCompatActivity() {

    private lateinit var binding: ActivityRegistMerchantBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityRegistMerchantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

}