package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseShowQr {

    @Expose
    @SerializedName("data")
    var data: Data? = null
    @Expose
    @SerializedName("status")
    var status: String? = null
    @Expose
    @SerializedName("error")
    var error: String? = null

    class Data {
        @Expose
        @SerializedName("qris_string")
        var qris_string: String? = null
    }
}
