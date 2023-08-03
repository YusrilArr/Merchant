package com.gpay.merchantapp.pages.home.receivepayment

import com.gpay.merchantapp.network.response.ResponseShowQr

interface ReceivePaymentView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccessQrCode(response: ResponseShowQr)
}