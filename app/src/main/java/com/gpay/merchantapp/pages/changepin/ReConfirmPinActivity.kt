package com.gpay.merchantapp.pages.changepin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.gpay.merchantapp.MainActivity
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseChangePassword
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.ConvertData
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityChangepinBinding
import com.gpay.merchantapp.network.response.ResponseLogin
import javax.inject.Inject

class ReConfirmPinActivity : BaseActivity(), ReConfirmPinView {
    @Inject
    lateinit var service: Service
    lateinit var presenter: ReConfirmPresenter
    var lastPass: String = ""
    var createPin = false

    var jml_pin = 0

    private lateinit var binding: ActivityChangepinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        binding.edtEnterpin.requestFocus()

        binding.txtTitlePin.text = resources.getString(R.string.str_re_confirm_new_pin)
        binding.txtSubtitlePin.text = resources.getString(R.string.str_re_confirm_new_pin)

        val newpin = intent.getStringExtra("newpin")
        createPin = intent.extras!!.getBoolean("createpin", false)

        if (createPin == false) {
            lastPass = intent.getStringExtra("pass").toString()
        }

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

        binding.btnLoginpin.setOnClickListener() {

            if (jml_pin == 0) {
                binding.btnLoginpin.isEnabled = false
                return@setOnClickListener
            }

            if (!binding.btnLoginpin.isEnabled) {
                return@setOnClickListener
            }

            if (binding.edtEnterpin.text.toString().equals(newpin)) {

                val pin = intent.extras!!.getString("newpin", "")
                val userName = intent.extras!!.getString("username", "")

                if (createPin) {
                    if (IsInternetAvailable(this)) {
                        presenter.createPin(userName, pin)
                    }
                } else {
                    intent.getStringExtra("pass")?.let { it1 ->
                        newpin?.let { it2 -> ConvertData().convertDataKey(it2) }?.let { it3 ->
                            presenter.postChangePassword(
                                it1,
                                it3
                            )
                        }
                    }
                }

            } else if (!binding.edtEnterpin.text.toString().equals(newpin)) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.PIN_is_not_match),
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtEnterpin.setText("")
            }
        }

        binding.actionBack.setOnClickListener {
            val intent = Intent(applicationContext, SetNewPinActivity::class.java)
            intent.putExtra("pass", lastPass)
            startActivity(intent)
            finish()
        }
    }

    fun init() {
        MainApp.instance.appComponent?.inject(this)
        presenter = ReConfirmPresenter(service, this)
    }

    override fun onErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        showLoading(true)
    }

    override fun hideLoading() {
        showLoading(false)
    }

    override fun onSuccesChangePassword(response: ResponseChangePassword) {
        Toast.makeText(
            this,
            resources.getString(R.string.str_changing_password),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onSuccesPin(response: ResponseLogin) {
        Toast.makeText(
            this,
            resources.getString(R.string.str_create_pin_awal),
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }, 2000)
    }
}