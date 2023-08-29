package com.gpay.merchantapp.pages.login

import android.os.Bundle
import com.gpay.merchantapp.R
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.utils.BaseActivity

class RegistrationActivity : BaseActivity(), RegistrationView {

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_merchant_registration)
        super.onCreate(savedInstanceState)
    }

    var currentMessage: String = ""

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

    override fun onSuccessRegistration(response: ResponseLogin) {
        TODO("Not yet implemented")
    }
}