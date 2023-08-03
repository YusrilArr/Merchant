package com.gpay.merchantapp.pages.contactus

import android.os.Bundle
import com.gpay.merchantapp.BuildConfig
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityContactusBinding
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.SharedPreferencesUtils

class ContactusActivity : BaseActivity(){

    private lateinit var binding: ActivityContactusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactusBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //contactViewedTracking()
        binding.actionBack.setOnClickListener {
            //contactBackTracking()
            finish()
        }

        binding.textEmail.setOnClickListener {
            openEmail()
            //contactEmailTracking()
        }

        binding.textNumberPhone.setOnClickListener {
            //contactPhoneTracking()
            openDialer()
        }

        binding.versiapps.text = BuildConfig.VERSION_NAME
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        //contactBackTracking()
    }

    private fun contactViewedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.mobile_number), "62"+ MainApp.instance.sharedPreferences?.getString(
            SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""))
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.contactus_viewed), bundle)
    }

    private fun contactBackTracking() {
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.mobile_number), "62"+MainApp.instance.sharedPreferences?.getString(
            SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""))
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.contactus_back_tapped), bundle)
    }

    private fun contactEmailTracking() {
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.mobile_number), "62"+MainApp.instance.sharedPreferences?.getString(
            SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""))
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.contactus_email_tapped), bundle)
    }

    private fun contactPhoneTracking() {
        MainApp.firebaseAnalytics.setUserProperty(getString(R.string.mobile_number), "62"+MainApp.instance.sharedPreferences?.getString(
            SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""))
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.contactus_phone_tapped), bundle)
    }
}