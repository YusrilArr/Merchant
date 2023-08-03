package com.gpay.merchantapp.pages.home.transhistory

import com.gpay.merchantapp.network.response.ResponseMsStd
import com.gpay.merchantapp.network.response.ResponseProfile
import com.gpay.merchantapp.network.response.ResponseTransactionList

interface TransHistoryView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesListTransaction(response: ResponseTransactionList)
    fun onSuccesListTransactionMore(response: ResponseTransactionList)
    fun onSuccesShowProfile(response: ResponseProfile)
    fun onSuccessMsStd(response: ResponseMsStd, id: String)
}