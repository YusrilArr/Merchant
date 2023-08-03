package com.gpay.merchantapp.utils

import android.util.Base64
import com.gpay.merchantapp.MainApp
import org.apache.commons.codec.binary.Hex
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class ConvertData {

    fun convertDataString(data: String): String {
//        System.out.println("data 1 = " + MainApp.instance.getK(1))
        var secret_token = MainApp.instance.sharedPreferences?.getString(SharedPreferencesUtils._KEY_TOKEN, "")
        var concat_secret_key = MainApp.instance.getK(1) + secret_token
        var sha256HMAC = Mac.getInstance("HmacSHA256")
        var secretKey = SecretKeySpec(concat_secret_key.toByteArray(Charsets.UTF_8), "HmacSHA256")
        sha256HMAC.init(secretKey)
        var hash = String(Hex().encode(sha256HMAC.doFinal(data.toByteArray(Charsets.UTF_8))))
        return hash
    }

    fun convertDataKey(data: String): String {
//        System.out.println("data 2 = " + MainApp.instance.getK(2))
        var sha256HMAC = Mac.getInstance("HmacSHA256")
        var secretKey = SecretKeySpec(MainApp.instance.getK(2).toByteArray(Charsets.UTF_8), "HmacSHA256")
        sha256HMAC.init(secretKey)
        var hash = String(Hex().encode(sha256HMAC.doFinal(data.toByteArray(Charsets.UTF_8))))
        return hash
    }

    fun convertCurency(nominal: String): HashMap<String, String> {

        var data = HashMap<String, String>()
        var replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol())

        val cleanString = nominal.replace(replaceable.toRegex(), "")
        val toRupiah = DecimalFormat("#,###")
        val formatAngka = DecimalFormatSymbols()
        formatAngka.setMonetaryDecimalSeparator(',')
        formatAngka.setGroupingSeparator('.')
        toRupiah.setDecimalFormatSymbols(formatAngka)

        data.put("normal", cleanString)
        data.put("format", toRupiah.format(cleanString.toLong()))

        return data

    }

    object Convert {
        fun convertQr(input: String): String {
//            System.out.println("data 3 = " + MainApp.instance.getK(3))
            val md = MessageDigest.getInstance("SHA-256") as MessageDigest
            md.update(MainApp.instance.getK(3).toByteArray(Charsets.UTF_8))
            var byteData: ByteArray = md.digest()
            var ivBytes = byteArrayOf(
                0x1.toByte(),
                0xf.toByte(),
                0x3.toByte(),
                0xe.toByte(),
                0x8.toByte(),
                0x7.toByte(),
                0x9.toByte(),
                0xa.toByte(),
                0xb.toByte(),
                0x5.toByte(),
                0x0.toByte(),
                0xf.toByte(),
                0x6.toByte(),
                0x2.toByte(),
                0x0.toByte(),
                0xc.toByte()
            )

            val skeySpec = SecretKeySpec(byteData, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            val ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)
            val raw = Base64.decode(input, Base64.DEFAULT)
            val originalBytes = ecipher.doFinal(raw)
            val original = String(originalBytes, Charsets.UTF_8)

            return original

        }

        fun encrypt(input: String): String {
            val md = MessageDigest.getInstance("SHA-256") as MessageDigest
            md.update(MainApp.instance.getK(3).toByteArray(Charsets.UTF_8))
            var byteData: ByteArray = md.digest()
            var ivBytes = byteArrayOf(
                0x1.toByte(),
                0xf.toByte(),
                0x3.toByte(),
                0xe.toByte(),
                0x8.toByte(),
                0x7.toByte(),
                0x9.toByte(),
                0xa.toByte(),
                0xb.toByte(),
                0x5.toByte(),
                0x0.toByte(),
                0xf.toByte(),
                0x6.toByte(),
                0x2.toByte(),
                0x0.toByte(),
                0xc.toByte()
            )

            val skeySpec = SecretKeySpec(byteData, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            val ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
            val originalBytes = ecipher.doFinal(input.toByteArray())
            val raw = Base64.encode(originalBytes, Base64.DEFAULT)
            val original = String(raw, Charsets.UTF_8)

            return original
        }
    }


}
