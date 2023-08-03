package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.*
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class CreatePinPresenter(val service: Service, val view: CreatePinView) {
    val subscriptions = CompositeSubscription()

    fun createPin(username: String, psw_hash: String) {

        if (!MainApp?.instance?.sharedPreferences?.getString(SharedPreferencesUtils._KEY_TOKEN, "")
                .equals("")
        ) {
            view.showLoading()
            service.postCreatePin(username, psw_hash, object : Service.CallbackPostLogin {
                override fun onError(networkError: String, status: String) {
                    view.hideLoading()
                    if (status != null) {
                        if (status.equals("2")) {
                            postGetToken(arrayOf(username, psw_hash), 1)
                        } else if (status.equals("3")) {
//                            MainApp.instance.onRegisterSessionExpired(networkError)
                            //postUpdateDeviceIdOtp(mobile_phone_no)
                        } else {
                            view.onErrorMessage(networkError)
                        }
                    }
                }

                override fun onSuccess(response: ResponseLogin) {
                    view.hideLoading()
                    MainApp.instance.sharedPreferences!!.edit()
                        .putString(
                            SharedPreferencesUtils._KEY_CUSTOMER_ID,
                            response.data!!.customerId
                        )
                        .putString(SharedPreferencesUtils._KEY_L_TOKEN, response.data!!.loginToken)
                        .commit()

                    MainApp.instance.sharedPreferences!!.edit()
                        .putBoolean(SharedPreferencesUtils._KEY_STATUS_LOGIN, true).commit()


                    view.onSuccesPin(response)
                }
            })
        } else {
            postGetToken(arrayOf(username, psw_hash), 1)
        }
    }

    fun postGetToken(args: Array<String>, type: Int) {
        service.postGetToken("merchant_app", object : Service.CallbackGetToken {
            override fun onError(networkError: String, status: String) {
                view.onErrorToken(networkError)
            }

            override fun onSuccess(response: ResponseGetToken) {
                if (response.status.equals("0")) {
                    MainApp.instance.sharedPreferences?.edit()
                        ?.putString(SharedPreferencesUtils._KEY_TOKEN, response?.secret_token)
                        ?.commit()
                    if (type == 1) {
                        createPin(args[0], args[1])
                    }
                }
            }
        })
    }
}