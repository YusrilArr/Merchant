package com.gpay.merchantapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


open class ThousandSeparatorTextWatcher(private val editText: EditText) : TextWatcher {

    private var cursorPosition = 0
    override fun beforeTextChanged(
        charSequence: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
        cursorPosition = editText.text.toString().length - editText.selectionStart
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {
        try {
            editText.removeTextChangedListener(this)
            val value = editText.text.toString()
            if (value != null && value != "") {
                if (value.startsWith(decimalMarker)) {
                    val text =
                        "0$decimalMarker"
                    editText.setText(text)
                }
                if (value.startsWith("0") && !value.startsWith("0$decimalMarker")) {
                    var index = 0
                    while (index < value.length && value[index] == '0') {
                        index++
                    }
                    var newValue = Character.toString(value[0])
                    if (index != 0) {
                        newValue = value[0].toString() + value.substring(index)
                    }
                    editText.setText(newValue)
                }
                val str = editText.text.toString().replace(thousandSeparator, "")
                if (value != "")
                {
                    editText.setText(getDecimalFormattedString(str))

                }

                editText.setSelection(editText.text.toString().length)
            }

            //setting the cursor back to where it was
            editText.setSelection(editText.text.toString().length - cursorPosition)
            editText.addTextChangedListener(this)
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText.addTextChangedListener(this)
        }
    }

    companion object {
        private lateinit var thousandSeparator: String
        private lateinit var decimalMarker: String

        private fun getDecimalFormattedString(value: String): String {
            val splitValue = value.split("\\.").toTypedArray()
            var beforeDecimal = value
            var afterDecimal: String? = null
            var finalResult = ""
            if (splitValue.size == 2) {
                beforeDecimal = splitValue[0]
                afterDecimal = splitValue[1]
            }
            var count = 0
            for (i in beforeDecimal.length - 1 downTo 0) {
                finalResult = beforeDecimal[i].toString() + finalResult
                count++
                if (count == 3 && i > 0) {
                    finalResult =
                        thousandSeparator + finalResult
                    count = 0
                }
            }
            if (afterDecimal != null) {
                finalResult =
                    finalResult + decimalMarker + afterDecimal
            }

            return finalResult
        }

        /*
        * Returns the string after removing all the thousands separators.
        * */
        fun getOriginalString(string: String): String {
            return string.replace(thousandSeparator, "")
        }
    }

    init {
        val localeID = Locale("in", "ID")
        val nf: NumberFormat = NumberFormat.getNumberInstance(localeID)
        var df = nf as DecimalFormat


        df.setDecimalSeparatorAlwaysShown(true)

        thousandSeparator = df.decimalFormatSymbols.groupingSeparator.toString()
        decimalMarker = df.decimalFormatSymbols.decimalSeparator.toString()
    }
}