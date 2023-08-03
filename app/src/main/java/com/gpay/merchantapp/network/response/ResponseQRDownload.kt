package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseQRDownload {

    @Expose
    @SerializedName("error")
    var error: String? = null
    @Expose
    @SerializedName("data")
    var data: Data? = null
    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data {
        @Expose
        @SerializedName("download_link")
        var downloadLink: String? = null
    }
}
