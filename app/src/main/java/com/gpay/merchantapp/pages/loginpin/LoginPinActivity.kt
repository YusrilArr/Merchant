package com.gpay.merchantapp.pages.loginpin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.google.firebase.messaging.FirebaseMessaging
import com.gpay.merchantapp.MainActivity
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityChangepinBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import javax.inject.Inject

class LoginPinActivity : BaseActivity(), LoginPinView {
    var pin: String = ""
    var currentMessage: String = ""
    var userName: String = ""
    var tokenId: String = ""

    private lateinit var binding: ActivityChangepinBinding

    @Inject
    lateinit var service: Service
    lateinit var presenter: LoginPinPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()

        binding.edtEnterpin.requestFocus()
        //presenter.getProfile()
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

        binding.txtTitlePin.text = resources.getString(R.string.str_input_login_pin_title)
        binding.txtSubtitlePin.text = resources.getString(R.string.str_input_login_pin)

        userName = MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_USERNAME, "").toString()

        binding.btnLoginpin.setOnClickListener {
            if (pin.equals("")) {
                binding.btnLoginpin.isEnabled = false
                return@setOnClickListener
            }
            if (!binding.btnLoginpin.isEnabled) {
                return@setOnClickListener
            }

            //loginScreenTracking(edt_mobile.text.toString())
            if (IsInternetAvailable(this)) {
                presenter.login(userName, pin)
            }
        }
        binding.actionBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun init() {
        MainApp.instance?.appComponent?.inject(this)
        presenter = LoginPinPresenter(service, this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
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

    override fun onSuccessLogin(response: ResponseLogin) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result != null && !TextUtils.isEmpty(task.result)) {
                        tokenId = task.result!!
                        presenter.registerToken(tokenId)
                    }
                }
            }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}