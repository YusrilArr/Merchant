package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSettlementList {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null


    inner class Datum {

        @SerializedName("settlement_status")
        @Expose
        var settlement_status: String? = null

        @SerializedName("transaction_count")
        @Expose
        var transaction_count: String? = null

        @SerializedName("transaction_amount")
        @Expose
        var transaction_amount: String? = null

        @SerializedName("transaction_date")
        @Expose
        var transaction_date: String? = null

        @SerializedName("settlement_date")
        @Expose
        var settlement_date: String? = null


      /*  @SerializedName("issuer")
        @Expose
        var issuer: String? = null

        @SerializedName("rrn")
        @Expose
        var rrn: String? = null*/
    }
}
