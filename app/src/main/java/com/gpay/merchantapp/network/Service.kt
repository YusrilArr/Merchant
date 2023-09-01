package com.gpay.merchantapp.network


import android.util.Log
import com.gpay.merchantapp.BuildConfig
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.network.response.*
import com.gpay.merchantapp.utils.ConvertData
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by adry.m on 19/06/20.
 */

@Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class Service(private val networkService: NetworkService) {

    interface BaseCallback {
        fun onError(message: String, status: String)
    }

    fun postGetToken(partner_name: String, callback: CallbackGetToken): Subscription {
        return networkService.postGetToken(partner_name).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseGetToken>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseGetToken) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun postPassword_hash(psw_hash: String, callback: Callbackverify_psw_hash): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()

        return networkService.verify_psw_hash(
            ConvertData().convertDataKey(psw_hash),
            ConvertData().convertDataString(
                ConvertData().convertDataKey(psw_hash) + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseVerifyPswHash>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseVerifyPswHash) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })

    }

    fun postLogin(
        username: String,
        psw_hash: String,
        callback: CallbackPostLogin
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postLogin(
            username,
            ConvertData().convertDataKey(psw_hash),
            ConvertData().convertDataString(ConvertData().convertDataKey(psw_hash) + username)
            , header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseLogin>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        System.out.println("Error Loginpin Exception : " + ex.message)
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseLogin) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun postCreatePin(
        username: String,
        psw_hash: String,
        callback: CallbackPostLogin
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postCreatePin(
            username,
            ConvertData().convertDataKey(psw_hash),
            ConvertData().convertDataString(username + ConvertData().convertDataKey(psw_hash))
            , header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseLogin>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseLogin) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun postValidateLogin(
        username: String,
        callback: CallbackPostLogin
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postValidateLogin(
            username,
            ConvertData().convertDataString(username)
            //ConvertData().convertDataString(ConvertData().convertDataKey(username))
            , header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseLogin>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseLogin) {
                    if (t.status.equals("0")) {

                        System.out.println("Response Login : " + t.status)
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }
    
//coba
    fun postNewMerchant(
        merchantData: Map<String, String>,
        callback: CallbackNewMerchant
    ): Subscription {
        print("=======")
        println(merchantData)
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        println("Test4")

        return networkService.postNewMerchant(
            merchantData["merchant_name"] ?: "",
            merchantData["marketing_name"] ?: "",
            merchantData["address"] ?: "",
            merchantData["city_name"] ?: "",
            merchantData["phone"] ?: "",
            merchantData["email"] ?: "",
            merchantData["contact_person"] ?: "",
            merchantData["zipcode"] ?: "",
            merchantData["tax_no"] ?: "",
//            merchantData["dt_create"] ?: "",
//            merchantData["is_del"] ?: "",
            merchantData["merchant_category"] ?: "",
//            merchantData["legal_entity_name"] ?: "",
            merchantData["bank_id"] ?: "",
            merchantData["bank_account_name"] ?: "",
            merchantData["bank_account_number"] ?: "",
            merchantData["province_name"] ?: "",
            merchantData["merchant_group_id"] ?: "",
            merchantData["filename_ktp"] ?: "",
            merchantData["filename_selfie"] ?: "",
            header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseNewMerchant>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseNewMerchant) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }
    fun postRegister(
        mobile_phone_no: String,
        email: String,
        first_name: String,
        sure_name: String,
        birth_date: String,
        callback: CallbackRegister
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postRegisterBeforeOtp(
            mobile_phone_no,
            email,
            first_name,
            sure_name,
            birth_date,
            ConvertData().convertDataString(birth_date + email + first_name + mobile_phone_no),
            header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseRegister>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseRegister) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun getProfile(callback: CallbackGetProfile): Subscription {
        val login_token =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "")
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] = login_token.toString()
        return networkService.postShowProfile(
            ConvertData().convertDataString(
               login_token.toString()
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseProfile>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseProfile) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun getQRDownloadLink(callback: CallbackGetQRDownload): Subscription {
        val login_token =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "")
        val header = HashMap<String, String>()
        header["Login-Token"] = login_token.toString()
        return networkService.qr_download(
            ConvertData().convertDataString(
                login_token.toString()
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseQRDownload>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseQRDownload) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun sendOTP(customer_id: String, callback: CallbackSendOtp): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postSendOtp(
            customer_id,
            ConvertData().convertDataString(customer_id), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseSendOtp>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseSendOtp) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun activateCustomer(
        customer_id: String,
        password: String,
        otp: String,
        callback: CallbackActivateCustomer
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.postActivateCustomer(
            customer_id,
            ConvertData().convertDataKey(password),
            otp,
            ConvertData().convertDataString(customer_id + ConvertData().convertDataKey(password) + otp),
            header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseActivateCustomer>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseActivateCustomer) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun showQR(
        amount: String,
        callback: CallbackShowQR
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.postShowQr(
            amount,
            ConvertData().convertDataString(
                MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                ) + amount
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseShowQr>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseShowQr) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun editProfile(
        first_name: String,
        sure_name: String,
        email: String,
        birth_date: String,
        callback: CallbackUpdateProfile
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.postUpdateProfile(
            first_name,
            sure_name,
            email,
            birth_date,
            ConvertData().convertDataString(
                first_name + sure_name + email + birth_date + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseEditProfile>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseEditProfile) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun changePassword(
        psw_hash: String,
        new_psw_hash: String,
        callback: CallbackChangePassword
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.postChangePassword(
            ConvertData().convertDataKey(
                psw_hash
            ),
            new_psw_hash,
            ConvertData().convertDataString(
                ConvertData().convertDataKey(
                    psw_hash
                ) + new_psw_hash + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseChangePassword>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseChangePassword) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun getProvinceList(callback: CallbackListProvince): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.getListProvinceList(
            ConvertData().convertDataString(
                MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                ).toString()
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseProvinceList>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseProvinceList) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun getCityList(province_name: String, callback: CallbackListCity): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.getCityList(
            province_name,
            ConvertData().convertDataString(
                province_name + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseCityList>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseCityList) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }

                    }
                }
            })
    }

    fun getTransactionList(
        date_start: String,
        date_end: String,
        page_no: String,
        page_size: String,
        callback: CallbackListTransaction
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.gettransactionList(
            date_start,
            date_end,
            page_no,
            page_size,
            ConvertData().convertDataString(
                page_no + page_size + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseTransactionList>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseTransactionList) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status!!.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                           callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun getSettlementList(
        date_start: String,
        date_end: String,
        callback: CallbackListSettlement
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.getsettlementList(
            date_start,
            date_end,
            ConvertData().convertDataString(
                date_start + date_end + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseSettlementList>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseSettlementList) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status!!.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
//                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }


    fun updateKyc(
        sure_name: String,
        gender_code: String,
        mother_maiden_name: String,
        idcard_number: String,
        place_of_birth: String,
        occupation_code: String,
        mailing_address: String,
        mailing_zipcode: String,
        mailing_city: String,
        mailing_province: String,
        mailing_country_code: String,
        idcard_address: String,
        idcard_zipcode: String,
        idcard_city: String,
        idcard_province: String,
        idcard_country_code: String,
        callback: CallbackUpdateKyc
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.updateKyc(
            sure_name,
            gender_code,
            idcard_number,
            place_of_birth,
            mother_maiden_name,
            occupation_code,
            mailing_address,
            mailing_zipcode,
            mailing_city,
            mailing_province,
            mailing_country_code,
            idcard_address,
            idcard_zipcode,
            idcard_city,
            idcard_province,
            idcard_country_code,
            ConvertData().convertDataString(
                idcard_number + mother_maiden_name + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseKycUpdate>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseKycUpdate) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    //Untuk Covid Donation Program
    fun getLinkCodeInfoQr(static_qr: String, callback: CallbackQrInfo): Subscription {
        Log.e("qr", "" + static_qr)

        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.static_linkcode_info(
            static_qr,
            ConvertData().convertDataString(
                static_qr + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseQrInfo>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseQrInfo) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun getInfoQr(static_qr: String, callback: CallbackQrInfo): Subscription {

        Log.e("qr", "" + static_qr)

        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.static_qr_info(
            static_qr,
            ConvertData().convertDataString(
                static_qr + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseQrInfo>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseQrInfo) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    //Covid Donation Payment
    fun postDonatePay(
        linkcode: String,
        amount: String,
        callback: CallbackQrPayment
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()

        return networkService.linkcode_payment_donation(
            linkcode, amount,
            ConvertData().convertDataString(
                linkcode + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN, ""
                ) + amount
            )
            , header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseQrPayment>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseQrPayment) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }

                    }
                }
            })
    }


    fun postQRpay(
        static_qr: String,
        amount: String,
        psw_hash: String,
        terminal_qris: String,
        dynamic_qr: String,
        tips_amount: String,
        callback: CallbackQrPayment
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()


        if (!terminal_qris.equals("")) {
            return networkService.static_qr_payment_terminal(
                amount, psw_hash, terminal_qris, dynamic_qr, tips_amount,
                ConvertData().convertDataString(
                    amount + psw_hash + MainApp.instance.sharedPreferences!!.getString(
                        SharedPreferencesUtils._KEY_L_TOKEN,
                        ""
                    )
                ), header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
                .subscribe(object : Subscriber<ResponseQrPayment>() {
                    override fun onCompleted() {
                        //event completed request
                    }

                    override fun onError(e: Throwable?) {
                        try {

                            callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onNext(t: ResponseQrPayment) {
                        if (t.status.equals("0")) {
                            callback.onSuccess(t)
                        } else {
                            if (t.status.equals("5")) {
                                MainApp.instance!!.onSessionExpired()
                            } else {
                                callback.onError(bahasa(t.error!!), t.status!!)
                            }

                        }
                    }
                })
        } else {
            return networkService.static_qr_payment(
                static_qr, amount, psw_hash, tips_amount,
                ConvertData().convertDataString(
                    amount + psw_hash + MainApp.instance.sharedPreferences!!.getString(
                        SharedPreferencesUtils._KEY_L_TOKEN,
                        ""
                    )
                ), header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
                .subscribe(object : Subscriber<ResponseQrPayment>() {
                    override fun onCompleted() {
                        //event completed request
                    }

                    override fun onError(e: Throwable?) {
                        try {
                            callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onNext(t: ResponseQrPayment) {
                        if (t.status.equals("0")) {
                            callback.onSuccess(t)
                        } else {
                            if (t.status.equals("5")) {
                                MainApp.instance!!.onSessionExpired()
                            } else {
                                callback.onError(bahasa(t.error!!), t.status!!)
                            }

                        }
                    }
                })
        }


    }

    fun postQRpayfp(
        static_qr: String,
        amount: String,
        terminal: String,
        dynamic_qr: String,
        tips_amount: String,
        callback: CallbackQrPayment
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()


        if (!terminal.equals("")) {
            return networkService.qr_payment_fp_terminal(
                terminal, amount, dynamic_qr, tips_amount,
                ConvertData().convertDataString(
                    amount + MainApp.instance.sharedPreferences!!.getString(
                        SharedPreferencesUtils._KEY_L_TOKEN,
                        ""
                    )
                ), header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
                .subscribe(object : Subscriber<ResponseQrPayment>() {
                    override fun onCompleted() {
                        //event completed request
                    }

                    override fun onError(e: Throwable?) {
                        try {

                            callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onNext(t: ResponseQrPayment) {
                        if (t.status.equals("0")) {
                            callback.onSuccess(t)
                        } else {
                            if (t.status.equals("5")) {
                                MainApp.instance!!.onSessionExpired()
                            } else {
                                callback.onError(bahasa(t.error!!), t.status!!)
                            }
                        }
                    }
                })
        } else {
            return networkService.qr_payment_fp(
                static_qr, amount, tips_amount,
                ConvertData().convertDataString(
                    amount + MainApp.instance.sharedPreferences!!.getString(
                        SharedPreferencesUtils._KEY_L_TOKEN,
                        ""
                    )
                ), header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
                .subscribe(object : Subscriber<ResponseQrPayment>() {
                    override fun onCompleted() {
                        //event completed request
                    }

                    override fun onError(e: Throwable?) {
                        try {

                            callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onNext(t: ResponseQrPayment) {
                        if (t.status.equals("0")) {
                            callback.onSuccess(t)
                        } else {
                            if (t.status.equals("5")) {
                                MainApp.instance!!.onSessionExpired()
                            } else {
                                callback.onError(bahasa(t.error!!), t.status!!)
                            }
                        }
                    }
                })
        }


    }

    fun getMsStd(data_group: String, callback: CallbackMsStd): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.ms_std(

            data_group,
            ConvertData().convertDataString(
                data_group + MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                )
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseMsStd>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseMsStd) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun forgetPassword(
        mobile_phone_no: String,
        birth_date: String,
        callback: CallbackForgetPassword
    ): Subscription {
        return networkService.forgetPassword(
            mobile_phone_no, birth_date,
            ConvertData().convertDataString(
                mobile_phone_no + birth_date
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseForgetPassword>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseForgetPassword) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun forgetPasswordchange(
        birth_date: String,
        otp: String,
        new_psw_hash: String,
        customer_id: String,
        callback: CallbackForgetPasswordChange
    ): Subscription {
        return networkService.forgetPasswordchange(
            birth_date,
            ConvertData().convertDataKey(new_psw_hash),
            customer_id,
            otp,
            ConvertData().convertDataString(
                birth_date + ConvertData().convertDataKey(new_psw_hash) + customer_id + otp
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseForgetPasswordChange>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseForgetPasswordChange) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }

            })
    }

    fun registerToken(
        token: String,
        device_lang: String,
        callback: Callbackverify_psw_hash
    ): Subscription {
        if (!MainApp.instance.sharedPreferences!!.getString(
                SharedPreferencesUtils._KEY_CUSTOMER_ID,
                ""
            ).equals("")
        ) {
            val header = HashMap<String, String>()
            header["Device-Id"] = MainApp.instance.uniquePsuedoID
            header["Login-Token"] =
                MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                ).toString()
            return networkService.register_token(
                token, device_lang, ConvertData().convertDataString(
                    MainApp.instance.sharedPreferences!!.getString(
                        SharedPreferencesUtils._KEY_L_TOKEN,
                        ""
                    ).toString()
                )
                , header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
                .subscribe(object : Subscriber<ResponseVerifyPswHash>() {
                    override fun onCompleted() {
                        //event completed request
                    }

                    override fun onError(e: Throwable?) {
                        try {
                            callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onNext(t: ResponseVerifyPswHash) {
                        if (t.status.equals("0")) {
                            callback.onSuccess(t)
                        } else {
                            if (t.status.equals("5")) {
                                MainApp.instance!!.onSessionExpired()
                            } else {
                                callback.onError(bahasa(t.error!!), t.status!!)
                            }

                        }
                    }
                })

        }

        return Subscriptions.unsubscribed()
    }

    fun post_update_device_id_otp(
        mobile_phone_no: String,
        callback: CallbackUpdateDeviceIdOtp
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.update_device_id_otp(
            mobile_phone_no,
            ConvertData().convertDataString(
                mobile_phone_no + MainApp.instance.uniquePsuedoID
            )
            , header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseUpdateDeviceIdOtp>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseUpdateDeviceIdOtp) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun postUpdateDeviceIdExec(
        customer_id: String,
        otp: String,
        callback: CallbackUpdateDeviceIdExec
    ): Subscription {
        val header = HashMap<String, String>()
        header["Device-Id"] = MainApp.instance.uniquePsuedoID
        return networkService.update_device_id_exec(
            customer_id, otp,
            ConvertData().convertDataString(
                otp + customer_id
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseUpdateDeviceIdExec>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseUpdateDeviceIdExec) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }

                }
            })
    }

    fun chkOtp(otp_enc: String, customer_id: String, callback: CallbackSendOtp): Subscription {
        return networkService.chk_otp(
            otp_enc,
            customer_id,
            ConvertData().convertDataString(
                otp_enc
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseSendOtp>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseSendOtp) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun chkOtpForget(
        otp_enc: String,
        mobile_phone_no: String,
        callback: CallbackSendOtp
    ): Subscription {
        return networkService.chk_otp_forget(
            otp_enc,
            mobile_phone_no,
            ConvertData().convertDataString(
                otp_enc
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseSendOtp>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseSendOtp) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }


    fun unforceChange(callback: CallbackSendOtp): Subscription {
        val header = HashMap<String, String>()
        header["Login-Token"] =
            MainApp.instance.sharedPreferences!!.getString(SharedPreferencesUtils._KEY_L_TOKEN, "").toString()
        return networkService.unforce_change(

            ConvertData().convertDataString(
                MainApp.instance.sharedPreferences!!.getString(
                    SharedPreferencesUtils._KEY_L_TOKEN,
                    ""
                ).toString()
            ), header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseSendOtp>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {

                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseSendOtp) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }

    fun appVersionCheck(callback: CallbackPostLogin): Subscription {
        val header = HashMap<String, String>()
        var version = BuildConfig.VERSION_NAME.substring(0, 3)
        header["App-Version"] = version
        return networkService.chk_app_version(version, header).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { t: Throwable? -> Observable.error(t) }
            .subscribe(object : Subscriber<ResponseLogin>() {
                override fun onCompleted() {
                    //event completed request
                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(bahasa(NetworkError(e).appErrorMessage), "")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseLogin) {
                    if (t.status.equals("0")) {
                        callback.onSuccess(t)
                    } else {
                        if (t.status.equals("5")) {
                            MainApp.instance!!.onSessionExpired()
                        } else {
                            callback.onError(bahasa(t.error!!), t.status!!)
                        }
                    }
                }
            })
    }


    fun bahasa(args: String): String {

        val separated = args.split("|").toTypedArray()
        if (separated.size > 1) {
            if (Locale.getDefault().isO3Language.equals("ind")) {
                return separated[0]
            } else {
                return separated[1]
            }
        } else {
            return separated[0]
        }
    }

    interface CallbackGetToken : BaseCallback {
        fun onSuccess(response: ResponseGetToken)
    }
    interface CallbackPostRegist : BaseCallback {
        fun onSuccess(response: ResponseRegister)
    }
    interface CallbackPostLogin : BaseCallback {
        fun onSuccess(response: ResponseLogin)
    }

    interface Callbackverify_psw_hash : BaseCallback {
        fun onSuccess(response: ResponseVerifyPswHash)
    }

    interface CallbackGetProfile : BaseCallback {
        fun onSuccess(response: ResponseProfile)
    }

    interface CallbackRegister : BaseCallback {
        fun onSuccess(response: ResponseRegister)
    }

    interface CallbackSendOtp : BaseCallback {
        fun onSuccess(response: ResponseSendOtp)
    }

    interface CallbackActivateCustomer : BaseCallback {
        fun onSuccess(response: ResponseActivateCustomer)
    }

    interface CallbackShowQR : BaseCallback {
        fun onSuccess(response: ResponseShowQr)
    }

    interface CallbackUpdateProfile : BaseCallback {
        fun onSuccess(response: ResponseEditProfile)
    }

    interface CallbackChangePassword : BaseCallback {
        fun onSuccess(response: ResponseChangePassword)
    }

    interface CallbackListProvince : BaseCallback {
        fun onSuccess(response: ResponseProvinceList)
    }

    interface CallbackListCity : BaseCallback {
        fun onSuccess(response: ResponseCityList)
    }

    interface CallbackListSettlement : BaseCallback {
        fun onSuccess(response: ResponseSettlementList)
    }

    interface CallbackListTransaction : BaseCallback {
        fun onSuccess(response: ResponseTransactionList)
    }

    interface CallbackUpdateKyc : BaseCallback {
        fun onSuccess(response: ResponseKycUpdate)
    }

    interface CallbackUploadPicKyc : BaseCallback {
        fun onSuccess(response: ResponseKycUploadPicture)
    }

    interface CallbackQrInfo : BaseCallback {
        fun onSuccess(response: ResponseQrInfo)
    }

    interface CallbackQrPayment : BaseCallback {
        fun onSuccess(response: ResponseQrPayment)
    }

    interface CallbackMsStd : BaseCallback {
        fun onSuccess(response: ResponseMsStd)
    }

    interface CallbackForgetPassword : BaseCallback {
        fun onSuccess(response: ResponseForgetPassword)
    }

    interface CallbackForgetPasswordChange : BaseCallback {
        fun onSuccess(response: ResponseForgetPasswordChange)
    }

    interface CallbackUpdateDeviceIdOtp : BaseCallback {
        fun onSuccess(response: ResponseUpdateDeviceIdOtp)
    }

    interface CallbackUpdateDeviceIdExec : BaseCallback {
        fun onSuccess(response: ResponseUpdateDeviceIdExec)
    }

    interface CallbackGetQRDownload : BaseCallback {
        fun onSuccess(response: ResponseQRDownload)
    }

    fun convertStringtoRequestBody(data: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }
    interface CallbackNewMerchant {
        fun onSuccess(response: ResponseNewMerchant)
        fun onError(errorMessage: String, statusCode: String)
    }


}
