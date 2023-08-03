package com.gpay.merchantapp

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.gpay.merchantapp.component.AppsComponent
import com.gpay.merchantapp.component.DaggerAppsComponent
import com.gpay.merchantapp.network.NetworkModule
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.pages.login.LoginActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import com.mutualmobile.androidkeystore.android.crypto.KeystoreCryptoFactory
import org.apache.commons.codec.binary.Hex
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class MainApp : MultiDexApplication() {
    @Inject
    lateinit var service: Service
    var appComponent: AppsComponent? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        KeystoreCryptoFactory.get(this).create_key_if_not_available("k4")
        KeystoreCryptoFactory.get(this).create_key_if_not_available("k5")
        KeystoreCryptoFactory.get(this).create_key_if_not_available("k6")
        KeystoreCryptoFactory.get(this)
            .encrypt("k4", "472177784932685841514f5e296639484f413042667e4d2333")
        KeystoreCryptoFactory.get(this)
            .encrypt("k5", "395f53615069656e535f30365f416973556e614d5f39313032")
        KeystoreCryptoFactory.get(this)
            .encrypt("k6", "7a21496e3563742a39485a4b76564a31305e5f386d734b54")
        super.onCreate()
        instance = this

        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(this,
            "secured_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        appComponent = DaggerAppsComponent.builder().networkModule(NetworkModule(this)).build()
        appComponent!!.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        openAppTracking()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        setupActivityListener()
    }

    companion object {
        lateinit var instance: MainApp set
        lateinit var customer_id: String set
        lateinit var firebaseAnalytics: FirebaseAnalytics
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            MainApp.instance.sharedPreferences?.edit()?.putString(
                SharedPreferencesUtils._KEY_LAT_LON,
                location.latitude.toString() + "," + location.longitude.toString()
            )?.apply()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun getK(type: Int): String {
        if (type == 1) {
            return String(
                Hex().decode(
                    KeystoreCryptoFactory.get(this).decrypt(
                        "k4",
                        KeystoreCryptoFactory.get(this).encrypt(
                            "k4",
                            "472177784932685841514f5e296639484f413042667e4d2333"
                        )
                    ).toByteArray()
                )
            )
        } else if (type == 2) {
            return String(
                Hex().decode(
                    KeystoreCryptoFactory.get(this).decrypt(
                        "k5",
                        KeystoreCryptoFactory.get(this).encrypt(
                            "k5",
                            "395f53615069656e535f30365f416973556e614d5f39313032"
                        )
                    ).toByteArray()
                )
            )
        } else {
            return String(
                Hex().decode(
                    KeystoreCryptoFactory.get(this).decrypt(
                        "k6",
                        KeystoreCryptoFactory.get(this).encrypt(
                            "k6",
                            "7a21496e3563742a39485a4b76564a31305e5f386d734b54"
                        )
                    ).toByteArray()
                )
            )
        }
    }

    fun onSessionExpired() {
//        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        MainApp.instance.sharedPreferences?.edit()?.clear()?.commit()
        var intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun onRegisterSessionExpired(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                var activeActivity = activity
                service.appVersionCheck(object : Service.CallbackPostLogin {
                    override fun onError(networkError: String, status: String) {
                        if (!networkError.contains("No Internet Connection!")) {
                            popupGagal(networkError, activeActivity!!)
                        } else {
                            popupGagal2(networkError, activeActivity!!)
                        }
                    }

                    override fun onSuccess(response: ResponseLogin) {

                    }
                })
            }


            override fun onActivityPaused(activity: Activity) {
                var activeActivity = null
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    // If all else fails, if the user does have lower than API 9 (lower
    // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
    // returns 'null', then simply the ID returned will be solely based
    // off their Android device information. This is where the collisions
    // can happen.
    // Thanks http://www.pocketmagic.net/?p=1662!
    // Try not to use DISPLAY, HOST or ID - these items could change.
    // If there are collisions, there will be overlapping data
    // Thanks to @Roman SL!
    // https://stackoverflow.com/a/4789483/950427
    // Only devices with API >= 9 have android.os.Build.SERIAL
    // http://developer.android.com/reference/android/os/Build.html#SERIAL
    // If a user upgrades software or roots their device, there will be a duplicate entry
    // Go ahead and return the serial for api => 9
    // String needs to be initialized
    // some listAllTags
    // Thanks @Joe!
    // https://stackoverflow.com/a/2853253/950427
    // Finally, combine the values we have found by using the UUID class to create a unique identifier
    val uniquePsuedoID: String get() {
        val m_szDevIDShort =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
        var serial: String? = null
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            return UUID(
                m_szDevIDShort.hashCode().toLong(),
                serial.hashCode().toLong()
            ).toString()
        } catch (exception: Exception) {
            serial = "serial"
        }

        return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
    }

    private fun openAppTracking() {

        val deviceType = Build.DEVICE
        val deviceFamily = Build.MODEL

        val carrierManager: TelephonyManager =
            applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val carrierName = carrierManager.networkOperatorName

        val currrentLanguage =
            if (Locale.getDefault().isO3Language.equals("eng")) "English" else "Indonesia"

        val appVersion = BuildConfig.VERSION_CODE.toString()

        val currentOS = "Android " + Build.VERSION.RELEASE

        val devID = MainApp.instance.uniquePsuedoID

        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.platform), "Android")
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.device_type), deviceType)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.device_family), deviceFamily)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.start_version), "1.0")
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.version), appVersion)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.carrier), carrierName)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.os), currentOS)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.language), currrentLanguage)
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.device_id), devID)
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.latlon),
            MainApp.instance.sharedPreferences?.getString(SharedPreferencesUtils._KEY_LAT_LON, "")
        )

        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.app_opened), bundle)
    }

    private fun popupGagal(message: String, activity: Activity) {
        val buttonClose: TextView
        val txt_title_popup: TextView
        val dialog = Dialog(activity)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_gagal_login)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        txt_title_popup = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        txt_title_popup.text = message
        buttonClose = dialog.findViewById<View>(R.id.action_close) as TextView
        buttonClose.visibility = View.INVISIBLE
        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun popupGagal2(message: String, activity: Activity) {
        val buttonClose: TextView
        val txt_title_popup: TextView
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.pop_up_gagal_login)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        txt_title_popup = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        var messageFilter = message
        if(messageFilter.toUpperCase().contains("Mobile phone number not registered".toUpperCase())){
            messageFilter = "Username not registered"
        }

        txt_title_popup.text = messageFilter
        buttonClose = dialog.findViewById<View>(R.id.action_close) as TextView
        buttonClose.visibility = View.VISIBLE
        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /*private fun popupUpdateGPLAY(message: String, activity: Activity) {
        val buttonClose: TextView
        val txt_title_popup: TextView
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.pop_up_gagal_login)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        txt_title_popup = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        txt_title_popup.text = message
        buttonClose = dialog.findViewById<View>(R.id.action_close) as TextView
        buttonClose.visibility = View.VISIBLE
        buttonClose.text = "Update"

        buttonClose.setOnClickListener {
            showGooglePlay()
        }

        dialog.show()
    }*/

    /*fun showGooglePlay() {
        try {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (anfe: ActivityNotFoundException) {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }*/
}
