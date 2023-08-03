package com.gpay.merchantapp.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {


    @Expose
    @SerializedName("error")
    private String msg;
    @Expose
    @SerializedName("status")
    private int code;
    @SuppressWarnings({"unused", "used by Retrofit"})
    public Response() {
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
