package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGetToken{
    @Expose
    @SerializedName("error")
    var error: String? = null
    @Expose
    @SerializedName("secret_token")
    var secret_token: String? = null
    @Expose
    @SerializedName("status")
    var status: String? = null
}
