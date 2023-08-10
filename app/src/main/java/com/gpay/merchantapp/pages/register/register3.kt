package com.gpay.merchantapp.pages.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
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
        // Referensi ke spinner di dalam layout
        val spinnerBank = binding.spinnerBank

        // Daftar pilihan bank
        val bankOptions = arrayOf("BANK NAME", "BCA", "BRI", "BNI", "BSI")

        // Buat adapter untuk spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter untuk spinner
        spinnerBank.adapter = adapter
    }
}