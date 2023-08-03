package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.network.response.ResponseVerifyPswHash

interface ChangePinView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesPassword(response: ResponseVerifyPswHash)
}