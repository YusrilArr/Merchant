package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseChangePassword
import com.gpay.merchantapp.network.response.ResponseGetToken
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class ReConfirmPresenter(val service: Service, val view: ReConfirmPinView) {
    val subscriptions = CompositeSubscription()

    fun postChangePassword(passOld: String, passNew: String) {
        view.showLoading()
        service.changePassword(passOld, passNew, object : Service.CallbackChangePassword {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(passOld, passNew), 1)
                    } else if (status.equals("3")) {
                        MainApp.instance.onSessionExpired()
                    } else {
                        view.onErrorMessage(networkError)
                    }
                }
            }

            override fun onSuccess(response: ResponseChangePassword) {
                view.hideLoading()
                view.onSuccesChangePassword(response)
            }
        })
    }

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
                            postGetToken(arrayOf(username, psw_hash), 2)
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
        view.showLoading()
        service.postGetToken("mobile", object : Service.CallbackGetToken {
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
                        postChangePassword(args[0], args[1])
                    } else {
                        createPin(args[0], args[1])
                    }
                }
            }
        })
    }
}
