package com.gpay.merchantapp.pages.loginpin

import com.gpay.merchantapp.network.response.ResponseLogin

interface LoginPinView {
    fun onErrorMessage(message: String)
    fun onErrorToken(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccessLogin(response: ResponseLogin)
}