package com.gpay.merchantapp.utils

import android.content.Context
import kotlin.math.sqrt

class DeviceUtils() {

    fun getDiagonalInches(ctx: Context): Boolean {

        return try {
            val displayMetrics = ctx.resources.displayMetrics
            val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
            val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInch = sqrt(xinch * xinch + yinch * yinch.toDouble())

            diagonalInch >= 7


        }catch (e: Exception){
            false
        }
    }
}