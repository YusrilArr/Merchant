package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseVerifyPswHash {

    @Expose
    @SerializedName("data")
    var data: String? = null
    @Expose
    @SerializedName("status")
    var status: String? = null
    @Expose
    @SerializedName("error")
    var error: String? = null
}
