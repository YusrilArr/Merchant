package com.gpay.merchantapp.network


import android.content.Context
import com.gpay.merchantapp.BuildConfig
import com.gpay.merchantapp.MainApp
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


/**
 * Created by GPay on 19/06/20.
 */
@Module
class NetworkModule(private val mainApp: MainApp) {

    @Provides
    @Singleton
    internal fun provideCall(): Retrofit {

        val certificatePinner = CertificatePinner.Builder()
            .add(
                "psmw.gpay.digital",
                "sha256/ugUSztyhCdvlZld/aUwp3ffDkU88JrFCK+7i9nFbICo="
            )// Diganti dengan GPAY
            .build()

        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        //val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        val sslSocketFactory: SSLSocketFactory =
            GetSSLConfig.getSSLConfig(MainApp.instance.applicationContext).socketFactory

        val okHttpClient = OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager)

            //val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json; charset = utf-8")
                    .build()
                val response = chain.proceed(request)
                response
            }.connectTimeout(30, TimeUnit.SECONDS)
            //.sslSocketFactory(GetSSLConfig.getSSLConfig(MainApp.instance!!.applicationContext).socketFactory)
            .readTimeout(30, TimeUnit.SECONDS)
            //.addInterceptor(LoggingInterceptor())
            .certificatePinner(certificatePinner)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun providesNetworkService(
        retrofit: Retrofit
    ): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun providesService(
        networkService: NetworkService
    ): Service {
        return Service(networkService)
    }

    @Provides
    @Singleton
    @ForApplication
    internal fun provideApplicationContext(): Context {
        return mainApp
    }

}
