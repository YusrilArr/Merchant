package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUpdateDeviceIdExec {

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
        @SerializedName("customer_id")
        var customerId: String? = null
    }
}
