package com.gpay.merchantapp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTransactionList {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @Expose
    @SerializedName("error")
    var error: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("total_data")
    @Expose
    var total_data: Int? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    /*class Data {
        @SerializedName("data")
        @Expose
        var data: List<Datum>? = null
    }*/

    inner class Datum {

        @SerializedName("tran_nr")
        @Expose
        var tran_nr: String? = null

        @SerializedName("transaction_time")
        @Expose
        var transaction_time: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("merchant_name")
        @Expose
        var merchant_name: String? = null

        @SerializedName("store_name")
        @Expose
        var store_name: String? = null

        @SerializedName("customer_id")
        @Expose
        var customer_id: String? = null

        @SerializedName("settlement_date")
        @Expose
        var settlement_date: String? = null

        @SerializedName("reference_number")
        @Expose
        var reference_number: String? = null

        @SerializedName("transaction_type")
        @Expose
        var transaction_type: String? = null

        @SerializedName("mobile_phone_no")
        @Expose
        var mobile_phone_no: String? = null

        @SerializedName("settlement_status")
        @Expose
        var settlement_status: String? = null

        @SerializedName("source_of_fund")
        @Expose
        var source_of_fund: String? = null

        @SerializedName("tran_type")
        @Expose
        var tran_type: String? = null
    }
}
