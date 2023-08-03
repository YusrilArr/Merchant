package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class Dummy {

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
}
