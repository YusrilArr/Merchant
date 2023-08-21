package com.gpay.merchantapp.network

import com.gpay.merchantapp.network.response.*
import retrofit2.http.*
import rx.Observable

/**
 * Created by GPay on 24/06/20.
 */

interface NetworkService {

    @FormUrlEncoded
    @POST("v1/peer/get_token")
    fun postGetToken(@Field("partner_name") partner_name: String): Observable<ResponseGetToken>

    @FormUrlEncoded
    @POST("v1/m_app/login")
    fun postLogin(
        @Field("username") username: String,
        @Field("psw_hash") psw_hash: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseLogin>

    @FormUrlEncoded
    @POST("v1/m_app/create_password")
    fun postCreatePin(
        @Field("username") username: String,
        @Field("psw_hash") psw_hash: String,
        @Field(
            "signature"
        ) signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseLogin>

    @FormUrlEncoded
    @POST("v1/m_app/validate_first_login")
    fun postValidateLogin(
        @Field("username") username: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseLogin>

    @FormUrlEncoded
    @POST("v3/mbl/register_before_otp")
    fun postRegisterBeforeOtp(
        @Field("mobile_phone_no") mobile_phone_no: String,
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field(
            "sure_name"
        ) sure_name: String,
        @Field("birth_date") birth_date: String,
        @Field(
            "signature"
        ) signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseRegister>

    @FormUrlEncoded
    @POST("v3/mbl/send_otp")
    fun postSendOtp(
        @Field("customer_id") customer_id: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseSendOtp>

    @FormUrlEncoded
    @POST("v3/mbl/activate_cust")
    fun postActivateCustomer(
        @Field("customer_id") customer_id: String,
        @Field("psw_hash") psw_hash: String,
        @Field("otp_enc") otp_enc: String,
        @Field(
            "signature"
        ) signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseActivateCustomer>

    @FormUrlEncoded
    @POST("v1/m_app/change_password")
    fun postChangePassword(
        @Field("psw_hash") psw_hash: String, @Field("new_psw_hash") new_psw_hash: String, @Field(
            "signature"
        ) signature: String, @HeaderMap headers: Map<String, String>
    ): Observable<ResponseChangePassword>

    @FormUrlEncoded
    @POST("v1/m_app/show_profile")
    fun postShowProfile(
        @Field("signature") signature: String, @HeaderMap headers: Map<String, String>
    ): Observable<ResponseProfile>

    @FormUrlEncoded
    @POST("v1/m_app/create_dynamic_qris")
    fun postShowQr(
        @Field("amount") amount: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseShowQr>

    @FormUrlEncoded
    @POST("v3/mbl/edit_profile")
    fun postUpdateProfile(
        @Field("first_name") first_name: String,
        @Field("sure_name") sure_name: String,
        @Field(
            "email"
        ) email: String,
        @Field("birth_date") birth_date: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseEditProfile>

    @FormUrlEncoded
    @POST("v3/mbl/province_list")
    fun getListProvinceList(
        @Field("signature") signature: String, @HeaderMap headers: Map<String, String>
    ): Observable<ResponseProvinceList>

    @FormUrlEncoded
    @POST("v3/mbl/city_list")
    fun getCityList(
        @Field("province_name") province_name: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseCityList>

    @FormUrlEncoded
    @POST("v3/mbl/kyc_update")
    fun updateKyc(
        @Field("sure_name") sure_name: String,
        @Field("gender_code") gender_code: String,
        @Field(
            "idcard_number"
        ) idcard_number: String,
        @Field("place_of_birth") place_of_birth: String,
        @Field("mother_maiden_name") mother_maiden_name: String,
        @Field(
            "occupation_code"
        ) occupation_code: String,
        @Field("mailing_address") mailing_address: String,
        @Field("mailing_zipcode") mailing_zipcode: String,
        @Field(
            "mailing_city"
        ) mailing_city: String,
        @Field("mailing_province") mailing_province: String,
        @Field("mailing_country_code") mailing_country_code: String,
        @Field(
            "idcard_address"
        ) idcard_address: String,
        @Field("idcard_zipcode") idcard_zipcode: String,
        @Field(
            "idcard_city"
        ) idcard_city: String,
        @Field("idcard_province") idcard_province: String,
        @Field("idcard_country_code") idcard_country_code: String,
        @Field(
            "signature"
        ) signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseKycUpdate>

    @FormUrlEncoded
    @POST("v3/mbl/qr_info")
    fun static_qr_info(
        @Field("qr") static_qr: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrInfo>

    //Covid Donation Program
    @FormUrlEncoded
    @POST("v3/mbl/linkcode_info")
    fun static_linkcode_info(
        @Field("linkcode") linkcode: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrInfo>

    @FormUrlEncoded
    @POST("v3/mbl/qr_payment")
    fun static_qr_payment(
        @Field("static_qr") static_qr: String,
        @Field("amount") amount: String,
        @Field(
            "psw_hash"
        ) psw_hash: String,
        @Field("tips_amount") tips_amount: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrPayment>


    //Covid Donation Program
    @FormUrlEncoded
    @POST("v3/mbl/linkcode_payment")
    fun linkcode_payment_donation(
        @Field("linkcode") linkcode: String,
        @Field("amount") amount: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrPayment>

    @FormUrlEncoded
    @POST("v3/mbl/qr_payment")
    fun static_qr_payment_terminal(
        @Field("amount") amount: String,
        @Field(
            "psw_hash"
        ) psw_hash: String,
        @Field("terminal_qris_code") terminal_qris_code: String,
        @Field(
            "dynamic_qr"
        ) dynamic_qr: String,
        @Field("tips_amount") tips_amount: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrPayment>

    @FormUrlEncoded
    @POST("v3/mbl/qr_payment_fp")
    fun qr_payment_fp(
        @Field("static_qr") static_qr: String,
        @Field("amount") amount: String,
        @Field("tips_amount") tips_amount: String,
        @Field(
            "signature"
        ) signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrPayment>

    @FormUrlEncoded
    @POST("v3/mbl/qr_payment_fp")
    fun qr_payment_fp_terminal(
        @Field("terminal_qris_code") terminal_qris_code: String,
        @Field("amount") amount: String,
        @Field(
            "dynamic_qr"
        ) dynamic_qr: String,
        @Field("tips_amount") tips_amount: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQrPayment>

    @FormUrlEncoded
    @POST("v3/mbl/ms_std")
    fun ms_std(
        @Field("data_group") data_group: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseMsStd>

    @FormUrlEncoded
    @POST("v3/mbl/forget_psw_otp")
    fun forgetPassword(
        @Field("mobile_phone_no") mobile_phone_no: String,
        @Field("birth_date") birth_date: String,
        @Field(
            "signature"
        ) signature: String
    ): Observable<ResponseForgetPassword>

    @FormUrlEncoded
    @POST("v3/mbl/forget_psw_exec")
    fun forgetPasswordchange(
        @Field("birth_date") birth_date: String,
        @Field("new_psw_hash") new_psw_hash: String,
        @Field("customer_id") customer_id: String,
        @Field("otp_enc") otp_enc: String,
        @Field("signature") signature: String
    ): Observable<ResponseForgetPasswordChange>

    @FormUrlEncoded
    @POST("v1/m_app/transaction_history")
    fun gettransactionList(
        @Field("date_start") date_start: String,
        @Field("date_end") date_end: String,
        @Field("page_no") page_no: String,
        @Field("page_size") page_size: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseTransactionList>

    //Settlement Summary
    @FormUrlEncoded
    @POST("v1/m_app/transaction_history_summary")
    fun getsettlementList(
        @Field("date_start") date_start: String,
        @Field("date_end") date_end: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseSettlementList>

    @FormUrlEncoded
    @POST("v1/m_app/verify_psw_hash")
    fun verify_psw_hash(
        @Field("psw_hash") psw_hash: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseVerifyPswHash>

    @FormUrlEncoded
    @POST("v1/m_app/update_firebase_token")
    fun register_token(
        @Field("firebase_token") firebase_token: String, @Field("device_lang") device_lang: String,
        @Field("signature") signature: String, @HeaderMap headers: Map<String, String>
    ): Observable<ResponseVerifyPswHash>

    @FormUrlEncoded
    @POST("v3/mbl/update_device_id_otp")
    fun update_device_id_otp(
        @Field("mobile_phone_no") mobile_phone_no: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseUpdateDeviceIdOtp>

    @FormUrlEncoded
    @POST("v3/mbl/update_device_id_exec")
    fun update_device_id_exec(
        @Field("customer_id") customer_id: String,
        @Field("otp_enc") otp: String,
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseUpdateDeviceIdExec>

    @FormUrlEncoded
    @POST("v3/mbl/chk_otp")
    fun chk_otp(
        @Field("otp_enc") otp_enc: String, @Field("customer_id") customer_id: String, @Field(
            "signature"
        ) signature: String
    ): Observable<ResponseSendOtp>

    @FormUrlEncoded
    @POST("v3/mbl/chk_otp")
    fun chk_otp_forget(
        @Field("otp_enc") otp_enc: String,
        @Field("mobile_phone_no") mobile_phone_no: String,
        @Field(
            "signature"
        ) signature: String
    ): Observable<ResponseSendOtp>

    @FormUrlEncoded
    @POST("v3/mbl/unforce_change")
    fun unforce_change(
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseSendOtp>

    @FormUrlEncoded
    @POST("v1/m_app/chk_app_version")
    fun chk_app_version(
        @Field("App-Version") version: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseLogin>

    @FormUrlEncoded
    @POST("v1/m_app/get_qr_download_link")
    fun qr_download(
        @Field("signature") signature: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseQRDownload>

    @FormUrlEncoded
    @POST("v1/m_app/insert")
    fun postNewMerchant(
        @Field("merchant_name") merchant_name: String,
        @Field("marketing_name") marketing_name: String,
        @Field("address") address: String,
        @Field("city_name") city_name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("contact_person") contact_person: String,
        @Field("zip_code") zip_code: String,
        @Field("tax_no") tax_no: String,
        @Field("dt_create") dt_create: String,
        @Field("is_del") is_del: String,
        @Field("merchant_category") merchant_category: String,
        @Field("legal_entity_name") legal_entity_name: String,
        @Field("bank_id") bank_name: String,
        @Field("bank_account_name") bank_account_name: String,
        @Field("bank_account_number") bank_account_number: String,
        @Field("province_name") provice_name: String,
        @Field("merchant_group_id") merchant_group_id: String,
        @Field("filename_ktp") filename_ktp: String,
        @Field("filename_selfie") filename_selfie: String,

        @HeaderMap headers: Map<String, String>
    ): Observable<ResponseNewMerchant>
}
