package com.gpay.merchantapp.pages.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister3Binding

//class register3 : AppCompatActivity() {
//    private lateinit var binding : ActivityRegister3Binding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegister3Binding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        binding.backReg3.setOnClickListener {
//            finish()
//        }
//        // Referensi ke spinner di dalam layout
//        val spinnerBank = binding.spinnerBank
//
//        // Daftar pilihan bank
//        val bankOptions = arrayOf("Select Bank", "BCA", "BRI", "BNI", "BSI", "Mandiri")
//
//        // Buat adapter untuk spinner
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        // Set adapter untuk spinner
//        spinnerBank.adapter = adapter
//    }
//}

class register3 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backReg3.setOnClickListener {
            finish()
        }

        val spinnerBank = binding.spinnerBank
        val bankOptions = arrayOf("Select Bank", "BCA", "BRI", "BNI", "BSI", "Mandiri")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBank.adapter = adapter

        binding.btnSubmitregister.setOnClickListener {
            // Handle button click here
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonStatus()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etOwnerbankname.addTextChangedListener(textWatcher)
        binding.etBanknumber.addTextChangedListener(textWatcher)
        spinnerBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateButtonStatus()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }

        // Initialize button state
        updateButtonStatus()
    }

    private fun updateButtonStatus() {
        val bankSelected = binding.spinnerBank.selectedItem.toString()
        val ownerName = binding.etOwnerbankname.text.toString().trim()
        val bankNumber = binding.etBanknumber.text.toString().trim()

        val isBankSelected = bankSelected != "Select Bank"
        val isFieldsFilled = isBankSelected && ownerName.isNotEmpty() && bankNumber.isNotEmpty()

        binding.btnSubmitregister.apply {
            isEnabled = isFieldsFilled
            alpha = if (isFieldsFilled) 1f else 0.5f
        }
    }
}
