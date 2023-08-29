package com.gpay.merchantapp.pages.register

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseGetToken
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.network.response.ResponseNewMerchant
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.Flow

class RegisterPresenter(private val view: RegisterView) {



    fun postNewMerchant(merchantData: Map<String, String>) {
        val callback = object : CallbackNewMerchant {
            override fun onSuccess(response: ResponseNewMerchant) {
                view.showSuccessMessage("Merchant registered successfully")
            }



            override fun onError(errorMessage: String, errorCode: String) {
                view.showErrorMessage(errorMessage)
            }
        }



        val subscription = ServiceManager.postNewMerchant(merchantData, callback)
        // You might want to keep a reference to this subscription if you need to manage it later.
    }
    object ServiceManager {



        fun postNewMerchant(merchantData: Map<String, String>, callback: CallbackNewMerchant): Subscription {
            // Implement the code for posting new merchant here
            // Make the network call using Retrofit or your preferred networking library
            // Call the appropriate callback methods based on the response

            // Example: Creating a mock response
            val mockResponse = ResponseNewMerchant("0", null)
            callback.onSuccess(mockResponse)

            // Return a Subscription if applicable
            return object : Subscription {
                override fun unsubscribe() {
                    // Handle unsubscription if needed
                }



                override fun isUnsubscribed(): Boolean {
                    // Return the unsubscription status
                    return false
                }
            }
        }
    }



    interface CallbackNewMerchant {
        fun onSuccess(response: ResponseNewMerchant)
        fun onError(errorMessage: String, errorCode: String)
    }



    class ResponseNewMerchant(val status: String, val error: String?)
}