package com.gpay.merchantapp.pages.login

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseGetToken
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class LoginPresenter(val service: Service, val view: LoginView) {
    val subscriptions = CompositeSubscription()

    fun validateLogin(username: String) {
        view.showLoading()
        service.postValidateLogin(username, object : Service.CallbackPostLogin {
            override fun onError(networkError: String, status: String) {
                view.hideLoading()
                if (status != null) {
                    if (status.equals("2")) {
                        postGetToken(arrayOf(username), 1)
                    } else if (status.equals("3")) {
//                            //MainApp.instance.onRegisterSessionExpired(networkError)
                    } else {
                        view.onErrorMessage(networkError)
                    }
                }
            }

            override fun onSuccess(response: ResponseLogin) {
                view.hideLoading()
                view.onSuccessValidateLogin(response)
            }
        })
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
                        validateLogin(args[0])
                    }
                }
            }
        })
    }
}