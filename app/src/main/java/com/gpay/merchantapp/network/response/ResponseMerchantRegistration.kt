package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMerchantRegistration {

    class ResponseLogin {

        @Expose
        @SerializedName("error")
        var error: String? = null
        @Expose
        @SerializedName("data")
        var data: DataEntity? = null
        @Expose
        @SerializedName("status")
        var status: String? = null
        @Expose
        @SerializedName("msg")
        var msg: String? = null
    }

}