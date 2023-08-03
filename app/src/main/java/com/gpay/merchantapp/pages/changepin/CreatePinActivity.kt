package com.gpay.merchantapp.pages.changepin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.gpay.merchantapp.MainActivity
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityChangepinBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import javax.inject.Inject

class CreatePinActivity : BaseActivity(), CreatePinView {
    lateinit var pin: String
    var userName: String = ""
    var currentMessage: String = ""

    private lateinit var binding: ActivityChangepinBinding

    @Inject
    lateinit var service: Service
    lateinit var presenter: CreatePinPresenter
    var cus_id = ""
    var pass_hash = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        binding.edtEnterpin.requestFocus()
        binding.edtEnterpin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val jml_pin = binding.edtEnterpin.length()
                if (jml_pin == 6) {
                    binding.btnLoginpin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_blue_corners10))
                    binding.btnLoginpin.isEnabled = true

                    pin = binding.edtEnterpin.text.toString()
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

        binding.txtTitlePin.text = resources.getString(R.string.str_create_pin)
        binding.txtSubtitlePin.text = resources.getString(R.string.str_create_pin)

        userName =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_USERNAME, "").toString()

        binding.btnLoginpin.setOnClickListener {

            val pin = binding.edtEnterpin.text.toString()

            val intentNewPin =
                Intent(applicationContext, ReConfirmPinActivity::class.java)
            intentNewPin.putExtra("newpin", pin)
            intentNewPin.putExtra("username", userName)
            intentNewPin.putExtra("createpin", true)
            startActivity(intentNewPin)
            finish()
        }

        binding.actionBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //finishAffinity()
    }

    fun init() {
        MainApp.instance?.appComponent?.inject(this)
        presenter = CreatePinPresenter(service, this)
    }

    override fun onErrorMessage(message: String) {
        if (message.equals("SQLSTATE[22P02]: Invalid text representation: 7 ERROR:  invalid input syntax for integer: \"\"")) {
            currentMessage = resources.getString(R.string.str_not_regis)
        } else {
            currentMessage = message
        }
        popupGagallogin(currentMessage)
    }

    override fun onErrorToken(message: String) {
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

    override fun onSuccesPin(response: ResponseLogin) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}