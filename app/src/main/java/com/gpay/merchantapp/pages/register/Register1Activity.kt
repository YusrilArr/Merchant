package com.gpay.merchantapp.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.gpay.merchantapp.databinding.ActivityRegister1Binding

class Register1Activity : AppCompatActivity(), RegisterView {



    private lateinit var binding: ActivityRegister1Binding
    private lateinit var presenter: RegisterPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        presenter = RegisterPresenter(this)



        binding.backReg1.setOnClickListener {
            finish()
        }



        val btnNext1 = binding.btnNext1



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
            val merchantData = mapOf(
                "merchant_name" to binding.etNameowner.text.toString(),
                "phone" to binding.etPhoneowner.text.toString(),
                "email" to binding.editTextTextEmailAddress2.text.toString(),
                "tax_no" to binding.etNpwp.text.toString()
                // Add other merchant data fields here...
            )
            presenter.postNewMerchant(merchantData)
        }



        updateButtonStatus(btnNext1, editTextList)
    }



    override fun showSuccessMessage(message: String) {
        // Display success message to the user
    }



    override fun showErrorMessage(message: String) {
        // Display error message to the user
    }



    private fun updateButtonStatus(button: Button, editTextList: List<EditText>) {
        val isAllFieldsFilled = editTextList.all { editText ->
            editText.text.toString().trim().isNotEmpty()
        }



        button.isEnabled = isAllFieldsFilled
        button.alpha = if (isAllFieldsFilled) 1f else 0.5f
    }
}