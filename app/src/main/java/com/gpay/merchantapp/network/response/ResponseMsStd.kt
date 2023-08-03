package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMsStd {

    @Expose
    @SerializedName("data")
    var data: List<DataEntity>? = null
    @Expose
    @SerializedName("error")
    var error: String? = null
    @Expose
    @SerializedName("status")
    var status: String? = null

    class DataEntity {
        @Expose
        @SerializedName("dsc")
        var dsc: String? = null
        @Expose
        @SerializedName("code")
        var code: String? = null
    }
}
