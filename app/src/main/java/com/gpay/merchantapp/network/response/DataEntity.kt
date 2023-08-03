package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataEntity {
    @Expose
    @SerializedName("login_token")
    var loginToken: String? = null
    @Expose
    @SerializedName("customer_id")
    var customerId: String? = null
    @Expose
    @SerializedName("otp_enc")
    var otp: String? = null
    @Expose
    @SerializedName("balance")
    var balance: String? = null
    @Expose
    @SerializedName("filename_selfie")
    var filenameSelfie: String? = null
    @Expose
    @SerializedName("filename_idcard")
    var filenameIdcard: String? = null
    @Expose
    @SerializedName("idcard_country_code")
    var idcardCountryCode: String? = null
    @Expose
    @SerializedName("idcard_province")
    var idcardProvince: String? = null
    @Expose
    @SerializedName("mailing_country_code")
    var mailingCountryCode: String? = null
    @Expose
    @SerializedName("mailing_province")
    var mailingProvince: String? = null
    @Expose
    @SerializedName("idcard_number")
    var idcardNumber: String? = null
    @Expose
    @SerializedName("blacklist_code")
    var blacklistCode: String? = null
    @Expose
    @SerializedName("dt_create")
    var dtCreate: String? = null
    @Expose
    @SerializedName("mobile_phone_no")
    var mobilePhoneNo: String? = null
    @Expose
    @SerializedName("password_hash")
    var passwordHash: String? = null
    @Expose
    @SerializedName("idcard_city")
    var idcardCity: String? = null
    @Expose
    @SerializedName("idcard_zipcode")
    var idcardZipcode: String? = null
    @Expose
    @SerializedName("idcard_address")
    var idcardAddress: String? = null
    @Expose
    @SerializedName("mailing_city")
    var mailingCity: String? = null
    @Expose
    @SerializedName("mailing_zipcode")
    var mailingZipcode: String? = null
    @Expose
    @SerializedName("mailing_address")
    var mailingAddress: String? = null
    @Expose
    @SerializedName("mother_maiden_name")
    var motherMaidenName: String? = null
    @Expose
    @SerializedName("place_of_birth")
    var placeOfBirth: String? = null
    @Expose
    @SerializedName("occupation_code")
    var occupationCode: String? = null
    @Expose
    @SerializedName("gender_code")
    var genderCode: String? = null
    @Expose
    @SerializedName("email")
    var email: String? = null
    @Expose
    @SerializedName("birth_date")
    var birthDate: String? = null
    @Expose
    @SerializedName("sure_name")
    var sureName: String? = null
    @Expose
    @SerializedName("first_name")
    var firstName: String? = null
    @Expose
    @SerializedName("id")
    var id: String? = null
    @Expose
    @SerializedName("qr_string")
    var qr_string: String? = null
    @Expose
    @SerializedName("terminal_id")
    var terminal_id: String? = null
    @Expose
    @SerializedName("terminal_name")
    var terminal_name: String? = null
    @Expose
    @SerializedName("store_id")
    var store_id: String? = null
    @Expose
    @SerializedName("store_name")
    var store_name: String? = null
    @Expose
    @SerializedName("merchant_id")
    var merchant_id: String? = null
    @Expose
    @SerializedName("merchant_name")
    var merchant_name: String? = null
    @Expose
    @SerializedName("transaction_id")
    var transaction_id: String? = null
    @Expose
    @SerializedName("dt_last_login")
    var dt_last_login: String? = null
    @Expose
    @SerializedName("dt_last_access")
    var dt_last_access: String? = null
    @Expose
    @SerializedName("customer_type_code")
    var customer_type_code: String? = null
    @Expose
    @SerializedName("card_status_code")
    var card_status_code: String? = null
    @Expose
    @SerializedName("amount")
    var amount: String? = null
    @Expose
    @SerializedName("terminal_qris_code")
    var terminal_qris_code: String? = ""
    @Expose
    @SerializedName("force_change")
    var force_change: String? = ""
    @Expose
    @SerializedName("tips_type")
    var tips_type: String? = ""
    @Expose
    @SerializedName("transaction_currency")
    var transaction_currency: String? = ""
    @Expose
    @SerializedName("tips_amount")
    var tips_amount: String? = ""
    @Expose
    @SerializedName("tips_pct")
    var tips_pct: String? = ""

    //Merchant App Data Entity Extention
    @Expose
    @SerializedName("username")
    var username: String? = null

    @Expose
    @SerializedName("name")
    var name: String? = null

    @Expose
    @SerializedName("address")
    var address: String? = null

    @Expose
    @SerializedName("phone1")
    var phone1: String? = null

    @Expose
    @SerializedName("phone2")
    var phone2: String? = null

    @Expose
    @SerializedName("flag_create_password")
    var flag_create_password: Boolean? = false

    @Expose
    @SerializedName("last_login_date")
    var last_login_date: String? = null

}
