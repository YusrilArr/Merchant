//package com.gpay.merchantapp.pages.register
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import com.gpay.merchantapp.R
//import com.gpay.merchantapp.databinding.ActivityRegister1Binding
//import com.gpay.merchantapp.databinding.ActivityRegister2Binding
//
//class register1 : AppCompatActivity() {
//
////    private lateinit var btnnext: Button
//
//    private lateinit var binding : ActivityRegister1Binding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegister1Binding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        binding.backReg1.setOnClickListener {
//            finish()
//        }
//
//
//        val btnnext1 = findViewById<Button>(R.id.btn_next1)
//
//        btnnext1.setOnClickListener {
//            Intent(this, register2::class.java).also {
//                startActivity(it)
//            }
//        }
//    }
//
//
//}

package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityRegister1Binding
import com.gpay.merchantapp.databinding.ActivityRegister2Binding

class register1 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backReg1.setOnClickListener {
            finish()
        }

        val btnNext1 = binding.btnNext1

        // Add a TextWatcher to each EditText
        val editTextList = listOf(
            binding.etNameowner,
            binding.etPhoneowner,
            binding.editTextTextEmailAddress2,
            binding.etNpwp
        )

        editTextList.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    updateButtonStatus(btnNext1, editTextList)
                }
            })
        }

        btnNext1.setOnClickListener {
            Intent(this@register1, register2::class.java).also {
                startActivity(it)
            }
        }

        // Initialize button status
        updateButtonStatus(btnNext1, editTextList)
    }

    private fun updateButtonStatus(button: Button, editTextList: List<EditText>) {
        val isAllFieldsFilled = editTextList.all { editText ->
            editText.text.toString().trim().isNotEmpty()
        }

        button.isEnabled = isAllFieldsFilled
        button.alpha = if (isAllFieldsFilled) 1f else 0.5f
    }
}
