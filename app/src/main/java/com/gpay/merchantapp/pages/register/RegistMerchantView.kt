package com.gpay.merchantapp.pages.register

import com.gpay.merchantapp.network.response.ResponseLogin

interface RegistMerchantView {
    fun onErrorMessage(message: String)
    fun onErrorToken(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccessRegistration(response: ResponseLogin)
}