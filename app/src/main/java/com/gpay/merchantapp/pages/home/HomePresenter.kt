package com.gpay.merchantapp.pages.home

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.*
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class HomePresenter(val service: Service, val view: HomeView) {
    val subscriptions = CompositeSubscription()

    fun transactionList(
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
                            postGetToken(arrayOf(date_start, date_end, page_no, page_size), 2)
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
    }

    fun transactionListMore(
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
                            postGetToken(arrayOf(""), 2)
                        } else if (status.equals("3")) {
                            MainApp.instance.onSessionExpired()
                        } else {
                            view.onErrorMessage(message)
                        }
                    }
                }

                override fun onSuccess(response: ResponseTransactionList) {
                    view.hideLoading()
                    view.onSuccesListTransactionMore(response)
                }

            })
    }

    fun getProfile() {
        //view.showLoading()
        service.getProfile(object : Service.CallbackGetProfile {
            override fun onError(networkError: String, status: String) {
                //view.hideLoading()
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
                //view.hideLoading()
                view.onSuccesShowProfile(response)
            }
        })
    }

    fun getQRDownloadLink() {
        view.showLoading()
        service.getQRDownloadLink(object : Service.CallbackGetQRDownload {
            override fun onError(networkError: String, status: String) {
                //view.hideLoading()
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(""), 3)
                    } else if (status.equals("3")) {
                        MainApp.instance.onSessionExpired()
                    } else {
                        view.onErrorMessage(networkError)
                    }
                }
            }

            override fun onSuccess(response: ResponseQRDownload) {
                //view.hideLoading()
                view.onSuccesQRDownload(response)
            }
        })
    }

    fun postGetToken(args: Array<String>, type: Int) {

        service.postGetToken("merchant_app", object : Service.CallbackGetToken {
            override fun onError(networkError: String, status: String) {
                view.onErrorMessage(networkError)
            }

            override fun onSuccess(response: ResponseGetToken) {

                if (response.status.equals("0")) {
                    MainApp.instance.sharedPreferences?.edit()
                        ?.putString(SharedPreferencesUtils._KEY_TOKEN, response?.secret_token)
                        ?.commit()
                    if (type == 1) {
                        getProfile()
                    } else if (type == 2) {
                        transactionList(args[0], args[1], args[2], args[3])
                    }else{
                        getQRDownloadLink()
                    }
                }
            }
        })
    }

}
