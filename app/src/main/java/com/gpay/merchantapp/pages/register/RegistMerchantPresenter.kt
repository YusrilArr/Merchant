package com.gpay.merchantapp.pages.register

import android.content.Intent
import android.widget.Toast
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseNewMerchant
import com.gpay.merchantapp.pages.login.LoginActivity

//import com.gpay.merchantapp.network.Service

class RegistMerchantPresenter(val service: Service,) {
    fun addMerchant(AddMerchant: Map<String, String>,) {
//        view.showLoading()
        println(AddMerchant)
        service.postNewMerchant(AddMerchant, object : Service.CallbackNewMerchant {
            override fun onError(networkError: String, status: String) {
                println("Pendaftaran Gagal")

//                view.hideLoading()
//                if (status != null) {
//                    if (status.equals("2")) {
//                        postGetToken(arrayOf(username, psw_hash), 1)
//                    } else if (status.equals("3")) {
////                            MainApp.instance.onRegisterSessionExpired(networkError)
//                        //postUpdateDeviceIdOtp(mobile_phone_no)
//                    } else {
//                        view.onErrorMessage(networkError)
//                    }
//                }
                val context = MainApp.instance.applicationContext
                Toast.makeText(context, "Pendaftaran Gagal, silakan coba lagi", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(response: ResponseNewMerchant) {
                println("Pendaftaran Berhasil")
//                view.hideLoading()
//                MainApp.instance.sharedPreferences!!.edit()
//                    .putString(
//                        SharedPreferencesUtils._KEY_CUSTOMER_ID,
//                        response.data!!.customerId
//                    )
//                    .putString(SharedPreferencesUtils._KEY_L_TOKEN, response.data!!.loginToken)
//                    .commit()
//
//                MainApp.instance.sharedPreferences!!.edit()
//                    .putBoolean(SharedPreferencesUtils._KEY_STATUS_LOGIN, true).commit()
//
//                view.onSuccessLogin(response)
                val context = MainApp.instance.applicationContext // Mengambil konteks dari aplikasi
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                Toast.makeText(context, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
            }
        })

    }
}