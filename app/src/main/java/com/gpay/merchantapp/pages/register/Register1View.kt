package com.gpay.merchantapp.pages.register

import com.gpay.merchantapp.network.response.ResponseLogin

interface RegisterView {
    fun showSuccessMessage(message: String)
    fun showErrorMessage(message: String)
}