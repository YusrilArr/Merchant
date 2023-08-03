package com.gpay.merchantapp.pages.changepin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityChangepinBinding
import com.gpay.merchantapp.utils.BaseActivity

class SetNewPinActivity : BaseActivity() {

    var lastPass: String = ""
    var jml_pin = 0

    private lateinit var binding: ActivityChangepinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.edtEnterpin.requestFocus()
        binding.txtTitlePin.text = resources.getString(R.string.str_change_pin)
        binding.txtSubtitlePin.text = resources.getString(R.string.txt_set_new_pin)

        lastPass = intent.extras!!.getString("pass").toString()

        binding.edtEnterpin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                jml_pin = binding.edtEnterpin.length()
                val pin = binding.edtEnterpin.text.toString()

                if (jml_pin == 6) {
                    binding.btnLoginpin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_blue_corners10))
                    binding.btnLoginpin.isEnabled = true
                } else {
                    binding.btnLoginpin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_grey_corners10))
                    binding.btnLoginpin.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        binding.btnLoginpin.setOnClickListener() {

            if (jml_pin == 0){
                binding.btnLoginpin.isEnabled = false
                return@setOnClickListener
            }

            if (!binding.btnLoginpin.isEnabled) {
                return@setOnClickListener
            }

            val pin = binding.edtEnterpin.text.toString()

            val intentNewPin =
                Intent(applicationContext, ReConfirmPinActivity::class.java)
            intentNewPin.putExtra("newpin", pin)
            intentNewPin.putExtra("pass", intent.extras!!.getString("pass", ""))
            intentNewPin.putExtra("createpin", false)
            startActivity(intentNewPin)
            finish()

            /* var result = StrengthPassword().passwordMeter(edt_enterpin.text.toString())

             if (Integer.parseInt(result.get(1)) < 5) {
                 onErrorMessage(resources.getString(R.string.str_stronger_pin))
                 edt_enterpin.setText("")
             } else {
                 val intentNewPin =
                     Intent(applicationContext, ReConfirmPinActivity::class.java)
                 intentNewPin.putExtra("newpin", pin)
                 intentNewPin.putExtra("pass", intent.extras.getString("pass", ""))
                 startActivity(intentNewPin)
                 finish()
             }*/
        }

        binding.actionBack.setOnClickListener {
            val intent = Intent(applicationContext, ChangePinActivity::class.java)
            intent.putExtra("pass", lastPass)
            startActivity(intent)
            finish()
        }
    }


    fun onErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}