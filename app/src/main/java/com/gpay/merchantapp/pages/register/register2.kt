package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister2Binding

//class register2 : AppCompatActivity() {
//
//    private lateinit var binding: ActivityRegister2Binding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegister2Binding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        binding.backReg2.setOnClickListener {
//            finish()
//        }
//
//        // Spinner for Category
//        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
//        val categories = arrayOf("Select Category", "Shopping", "Groceries", "Donate")
//        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        categorySpinner.adapter = categoryAdapter
//
//        categorySpinner.setSelection(0, false) // Set initial selection to the hint item
//
//        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedItem = parent?.getItemAtPosition(position).toString()
//
//                if (selectedItem != "Select Category") {
//                    // Handle the selected category here
//                    // For example, you can display a Toast message
//                    Toast.makeText(this@register2, "Selected Category: $selectedItem", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Handle case where nothing is selected (optional)
//            }
//        }
//
//
//        // Spinner for Quantity
//        val qtySpinner = findViewById<Spinner>(R.id.spinner_qty)
//        val quantities = arrayOf(" Qty","1", "2 - 5", "6 - 10", " > 10")
//        val qtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities)
//        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        qtySpinner.adapter = qtyAdapter
//
//        val btnnext2 = findViewById<Button>(R.id.btn_next2)
//        btnnext2.setOnClickListener {
//            Intent(this, register3::class.java).also {
//                startActivity(it)
//            }
//        }
//    }
//}

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

        // Spinner for Category
        val categorySpinner = binding.spinnerCategory
        val categories = arrayOf("Select Category", "Shopping", "Groceries", "Donate")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.setSelection(0, false) // Set initial selection to the hint item

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateButtonStatus()

                val selectedItem = parent?.getItemAtPosition(position).toString()

                if (selectedItem != "Select Category") {
                    Toast.makeText(this@register2, "Selected Category: $selectedItem", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }

        // Spinner for Quantity
        val qtySpinner = binding.spinnerQty
        val quantities = arrayOf(" Qty", "1", "2 - 5", "6 - 10", " > 10")
        val qtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities)
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        qtySpinner.adapter = qtyAdapter

        qtySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateButtonStatus()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }

        binding.btnNext2.setOnClickListener {
            Intent(this, register3::class.java).also {
                startActivity(it)
            }
        }

        updateButtonStatus()
    }

    private fun updateButtonStatus() {
        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
        val selectedQuantity = binding.spinnerQty.selectedItem.toString()
        val brandName = binding.etBrandName.text.toString().trim()
        val emailMerchant = binding.etEmailMerchant.text.toString().trim()
        val addressMerchant = binding.etAddressMerchant.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val postalCode = binding.etPostalcode.text.toString().trim()

        val isCategorySelected = selectedCategory != "Select Category"
        val isQuantitySelected = selectedQuantity != " Qty"
        val isFieldsFilled =
            isCategorySelected && isQuantitySelected && brandName.isNotEmpty() && emailMerchant.isNotEmpty() &&
                    addressMerchant.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()

        binding.btnNext2.apply {
            isEnabled = isFieldsFilled
            alpha = if (isFieldsFilled) 1f else 0.5f
        }
    }
}

