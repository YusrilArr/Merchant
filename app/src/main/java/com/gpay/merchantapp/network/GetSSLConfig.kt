package com.gpay.merchantapp.network

import android.content.Context
import com.gpay.merchantapp.R
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory

object GetSSLConfig {

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        KeyManagementException::class
    )
    internal fun getSSLConfig(context: Context): SSLContext {

        // Loading CAs from an InputStream
        var cf: CertificateFactory? = null
        cf = CertificateFactory.getInstance("X.509")

        var ca: Certificate? =null
        // I'm using Java7. If you used Java6 close it manually with finally.
        context.resources.openRawResource(R.raw.gpay).use { cert -> ca = cf!!.generateCertificate(cert) }

        // Creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // Creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        return sslContext
    }
}