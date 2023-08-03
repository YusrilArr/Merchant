package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCityList {

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
        @SerializedName("province_name")
        var provinceName: String? = null
        @Expose
        @SerializedName("city_name")
        var cityName: String? = null
    }
}
