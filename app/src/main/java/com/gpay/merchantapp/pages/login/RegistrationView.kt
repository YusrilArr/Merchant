package com.gpay.merchantapp.pages.login

import com.gpay.merchantapp.network.response.ResponseLogin

interface RegistrationView {
    fun onErrorMessage(message: String)
    fun onErrorToken(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccessRegistration(response: ResponseLogin)
}