package com.gpay.merchantapp.pages.changepin

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.*
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription

class ChangePinPresenter(val service: Service, val view: ChangePinView) {
    val subscriptions = CompositeSubscription()

    fun postPassword(psw_hash: String){
        if (!MainApp?.instance?.sharedPreferences?.getString(SharedPreferencesUtils._KEY_TOKEN, "").equals("")) {
            view.showLoading()
            service.postPassword_hash(psw_hash, object : Service.Callbackverify_psw_hash {
                override fun onSuccess(response: ResponseVerifyPswHash) {
                    view.hideLoading()
                    view.onSuccesPassword(response)
                }

                override fun onError(networkError: String, status: String) {
                    view.hideLoading()
                    if (status != null) {
                        if (status.equals("2")) {
                            postGetToken(arrayOf(psw_hash))
                        }else if (status.equals("3")){
                            MainApp.instance.onSessionExpired()
                        } else{
                            view.onErrorMessage(networkError)
                        }
                    }
                }
            })
        }
    }

    fun postGetToken(args: Array<String> ) {
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
                        ?.putString(SharedPreferencesUtils._KEY_TOKEN, response?.secret_token)?.commit()
                    postPassword(args[0])
                }
            }
        })
    }
}