package com.gpay.merchantapp.pages.changepin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityChangepinBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseVerifyPswHash
import com.gpay.merchantapp.utils.BaseActivity
import javax.inject.Inject

class ChangePinActivity : BaseActivity(), ChangePinView {
    var currentMessage: String = ""
    var jml_pin = 0

    @Inject
    lateinit var service: Service
    lateinit var presenter: ChangePinPresenter
    var cus_id = ""
    var pass_hash = ""

    private lateinit var binding: ActivityChangepinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        binding.edtEnterpin.requestFocus()

        binding.edtEnterpin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                jml_pin = binding.edtEnterpin.length()
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

        binding.btnLoginpin.setOnClickListener {

            if (jml_pin == 0){
                binding.btnLoginpin.isEnabled = false
                return@setOnClickListener
            }

            if (!binding.btnLoginpin.isEnabled) {
                return@setOnClickListener
            }

            if (IsInternetAvailable(this)) {
                presenter.postPassword(binding.edtEnterpin.text.toString())
            }
        }

        binding.txtTitlePin.text = resources.getString(R.string.str_change_pin)
        binding.txtSubtitlePin.text = resources.getString(R.string.str_input_old_pin)

        binding.actionBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun init() {
        MainApp.instance?.appComponent?.inject(this)
        presenter = ChangePinPresenter(service, this)
    }

    override fun onErrorMessage(message: String) {
        if (message.equals("SQLSTATE[22P02]: Invalid text representation: 7 ERROR:  invalid input syntax for integer: \"\"")) {
            currentMessage = resources.getString(R.string.str_not_regis)
        } else {
            currentMessage = message
        }
        popupGagallogin(currentMessage)
    }

    override fun showLoading() {
        showLoading(true)
    }

    override fun hideLoading() {
        showLoading(false)
    }

    override fun onSuccesPassword(response: ResponseVerifyPswHash) {
        val intent = Intent(applicationContext, SetNewPinActivity::class.java)
        intent.putExtra("pass",binding.edtEnterpin.text.toString())
        startActivity(intent)
        finish()
    }
}