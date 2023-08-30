
package com.gpay.merchantapp.pages.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegistMerchantBinding
import java.io.IOException

class RegistMerchantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistMerchantBinding

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            // Check the file extension before displaying the image
            if (imageUri != null && (imageUri.toString().endsWith(".jpg") || imageUri.toString().endsWith(".png"))) {
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    // Display the bitmap in the appropriate ImageView
                    binding.ivKtp.setImageBitmap(bitmap) // Example, replace with your ImageView
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                // Handle the case where the selected file is not a valid image
                // Display an error message or take appropriate action
                Toast.makeText(this, "Please select a valid JPG or PNG image.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistMerchantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnKtp.setOnClickListener {
            pickImage.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }

        binding.btnSelfie.setOnClickListener {
            pickImage.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }

        binding.btnLogo.setOnClickListener {
            pickImage.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }

        binding.button2.setOnClickListener {
            pickImage.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }


        binding.backBtn.setOnClickListener {
            finish()
        }

        val qtyOptions = arrayOf("Qty", "1", "2 - 5", "6 - 10", "> 10")
        setupSpinner(binding.spnQty, qtyOptions.toList(), "Qty")

        val categoryOptions = listOf("Select Category", "Shopping", "Groceries", "Donate")
        setupSpinner(binding.spnCategory, categoryOptions, "Select Category")

        val bankOptions = listOf("Select Bank", "BCA", "BRI", "BNI", "BSI", "Mandiri")
        setupSpinner(binding.spnBank, bankOptions, "Select Bank")

        // Disable submit button by default and set its style
        binding.btnSubmit.isEnabled = false
        binding.btnSubmit.setBackgroundResource(R.drawable.bg_grey_corners10) // Set the initial disabled background

        // Attach text change listeners to input fields
        binding.etMerchantname.addTextChangedListener(textWatcher)
        binding.etMerchantemail.addTextChangedListener(textWatcher)
        // Add other input fields...

        binding.btnSubmit.setOnClickListener {
            // Handle submit button click
        }
    }

    private fun setupSpinner(spinner: Spinner, options: List<String>, placeholder: String) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(0, false) // Set selection to the placeholder without triggering listener

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinner.getItemAtPosition(position) as String
                if (selectedItem == placeholder) {
                    // Do nothing or display an error message
                } else {
                    // Handle the selected item
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateSubmitButtonState()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updateSubmitButtonState() {
        val merchantName = binding.etMerchantname.text.toString().trim()
        val merchantEmail = binding.etMerchantemail.text.toString().trim()
        // Get values from other input fields...

        val isNotEmpty = merchantName.isNotEmpty() && merchantEmail.isNotEmpty()
        binding.btnSubmit.isEnabled = isNotEmpty

        // Set button background based on enable state
        val backgroundResource = if (isNotEmpty) R.drawable.bg_blue_corners10 else R.drawable.bg_grey_corners10
        binding.btnSubmit.setBackgroundResource(backgroundResource)
    }
}
