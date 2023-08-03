package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.network.response.ResponseLogin

interface CreatePinView {
    fun onErrorMessage(message: String)
    fun onErrorToken(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesPin(response: ResponseLogin)
}