package com.gpay.merchantapp.pages.home.settlementhistory

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseGetToken
import com.gpay.merchantapp.network.response.ResponseMsStd
import com.gpay.merchantapp.network.response.ResponseProfile
import com.gpay.merchantapp.network.response.ResponseSettlementList
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class SettlementHistoryPresenter(val service: Service, val view: SettlementHistoryView) {
    val subscriptions = CompositeSubscription()

    /*fun transactionList(
        date_start: String,
        date_end: String,
        page_no: String,
        page_size: String
    ) {
        view.showLoading()
        service.getTransactionList(
            date_start,
            date_end,
            page_no,
            page_size,
            object : Service.CallbackListTransaction {
                override fun onError(message: String, status: String) {
                    view.hideLoading()
                    if (status != null) {
                        if (status.equals("2")) {
                            postGetToken(arrayOf(""), 1)
                        } else if (status.equals("3")) {
                            MainApp.instance.onSessionExpired()
                        } else {
                            view.onErrorMessage(message)
                        }
                    }
                }

                override fun onSuccess(response: ResponseTransactionList) {
                    view.hideLoading()
                    view.onSuccesListTransaction(response)
                }

            })
    }*/

    fun settlementList(
        date_start: String,
        date_end: String
    ) {
        view.showLoading()
        service.getSettlementList(
            date_start,
            date_end,
            object : Service.CallbackListSettlement {
                override fun onError(message: String, status: String) {
                    view.hideLoading()
                    if (status != null) {
                        if (status.equals("2")) {
                            postGetToken(arrayOf(""), 1)
                        } else if (status.equals("3")) {
                            MainApp.instance.onSessionExpired()
                        } else {
                            view.onErrorMessage(message)
                        }
                    }
                }

                override fun onSuccess(response: ResponseSettlementList) {
                    view.hideLoading()
                    view.onSuccesListTransaction(response)
                }

            })
    }

    fun settlementListMore(
        date_start: String,
        date_end: String
    ) {
        view.showLoading()
        service.getSettlementList(
            date_start,
            date_end,
            object : Service.CallbackListSettlement {
                override fun onError(message: String, status: String) {
                    view.hideLoading()
                    if (status != null) {
                        if (status.equals("2")) {
                            postGetToken(arrayOf(""), 2)
                        } else if (status.equals("3")) {
                            MainApp.instance.onSessionExpired()
                        } else {
                            view.onErrorMessage(message)
                        }
                    }
                }

                override fun onSuccess(response: ResponseSettlementList) {
                    view.hideLoading()
                    view.onSuccesListTransactionMore(response)
                }

            })
    }

    fun postGetToken(array: Array<String>, type: Int) {
        view.showLoading()
        service.postGetToken("merchant_app", object : Service.CallbackGetToken {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                view.onErrorMessage(networkError)
            }

            override fun onSuccess(response: ResponseGetToken) {
                view.hideLoading()
                if (response.status.equals("0")) {
                    MainApp.instance.sharedPreferences?.edit()
                        ?.putString(SharedPreferencesUtils._KEY_TOKEN, response?.secret_token)
                        ?.commit()
                    if (type == 1) {
                       settlementList(array[0],array[1])
                    } else if (type == 2) {
                        settlementListMore(array[0],array[1])
                    }

                }
            }
        })
    }

    fun getProfile() {

        view.showLoading()
        service.getProfile(object : Service.CallbackGetProfile {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(""), 1)
                    } else if (status.equals("3")) {
                        MainApp.instance.onSessionExpired()
                    } else {
                        view.onErrorMessage(networkError)
                    }
                }
            }

            override fun onSuccess(response: ResponseProfile) {
                view.hideLoading()
                view.onSuccesShowProfile(response)
            }
        })

    }

    fun getCardStatus(id: String) {
        service.getMsStd("card_status", object : Service.CallbackMsStd {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                view.onErrorMessage(networkError)
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(id), 2)
                    }
                }
            }

            override fun onSuccess(response: ResponseMsStd) {
                view.hideLoading()
                view.onSuccessMsStd(response, id)
            }
        })
    }
}