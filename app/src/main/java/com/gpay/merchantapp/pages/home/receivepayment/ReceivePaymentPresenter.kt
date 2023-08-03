package com.gpay.merchantapp.pages.home.receivepayment

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseGetToken
import com.gpay.merchantapp.network.response.ResponseShowQr
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class ReceivePaymentPresenter(val service: Service, val view: ReceivePaymentView) {
    val subscriptions = CompositeSubscription()

    fun showQr(amount: String) {
        view.showLoading()
        service.showQR(amount, object : Service.CallbackShowQR {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(amount), 1)
                    } else if (status.equals("3")) {
                        MainApp.instance.onSessionExpired()
                    } else {
                        view.onErrorMessage(networkError)
                    }
                }

            }

            override fun onSuccess(response: ResponseShowQr) {
                view.hideLoading()
                view.onSuccessQrCode(response)
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
                        showQr(args[0])
                    }
                }
            }
        })
    }
}