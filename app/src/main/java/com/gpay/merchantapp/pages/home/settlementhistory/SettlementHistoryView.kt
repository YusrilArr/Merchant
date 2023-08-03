package com.gpay.merchantapp.pages.home.settlementhistory

import com.gpay.merchantapp.network.response.ResponseMsStd
import com.gpay.merchantapp.network.response.ResponseProfile
import com.gpay.merchantapp.network.response.ResponseSettlementList

interface SettlementHistoryView {
    fun onErrorMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSuccesListTransaction(response: ResponseSettlementList)
    fun onSuccesListTransactionMore(response: ResponseSettlementList)
    fun onSuccesShowProfile(response: ResponseProfile)
    fun onSuccessMsStd(response: ResponseMsStd, id: String)
}