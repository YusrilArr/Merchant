package com.gpay.merchantapp.pages.login

import com.gpay.merchantapp.network.response.ResponseLogin

interface LoginView {
    fun onErrorMessage(message: String)
    fun onErrorToken(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccessValidateLogin(response: ResponseLogin)
}