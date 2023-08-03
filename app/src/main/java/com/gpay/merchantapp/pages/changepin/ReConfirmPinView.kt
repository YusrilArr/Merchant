package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.network.response.ResponseChangePassword
import com.gpay.merchantapp.network.response.ResponseLogin

interface ReConfirmPinView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesChangePassword(response: ResponseChangePassword)
    fun onSuccesPin(response: ResponseLogin)
}