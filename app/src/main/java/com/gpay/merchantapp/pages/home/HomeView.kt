package com.gpay.merchantapp.pages.home

import com.gpay.merchantapp.network.response.*

interface HomeView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesShowProfile(response: ResponseProfile)
    fun onSuccesListTransaction(response: ResponseTransactionList)
    fun onSuccesListTransactionMore(response: ResponseTransactionList)
    fun onSuccesQRDownload(response: ResponseQRDownload)
}