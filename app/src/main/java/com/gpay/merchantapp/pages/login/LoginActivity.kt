package com.gpay.merchantapp.pages.login

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gpay.merchantapp.BuildConfig
import com.gpay.merchantapp.MainActivity
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityLoginBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseLogin
import com.gpay.merchantapp.pages.changepin.CreatePinActivity
import com.gpay.merchantapp.pages.contactus.ContactusActivity
import com.gpay.merchantapp.pages.contactus.ForgetPinActivity
import com.gpay.merchantapp.pages.loginpin.LoginPinActivity
import com.gpay.merchantapp.pages.register.RegistMerchantActivity
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import java.util.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginView {
    var userName: String = ""
    var currentMessage: String = ""


    @Inject
    lateinit var service: Service
    lateinit var presenter: LoginPresenter
    private lateinit var binding: ActivityLoginBinding

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_CUSTOMER_ID,
                ""
            )!!.isNotEmpty() && MainApp.instance.sharedPreferences!!.getBoolean(
                SharedPreferencesUtils._KEY_STATUS_LOGIN,
                false
            )
        ) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.edtMobile.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun afterTextChanged(s: Editable?) {
                if (!binding.edtMobile.text.toString().isNullOrEmpty()) {

                    var textLengh = binding.edtMobile.text.toString().length

                    if (textLengh < 6) {
                        binding.btnSignin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_grey_corners10))
                        binding.btnSignin.isEnabled = false
                    } else {
                        binding.btnSignin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_blue_corners10))
                        binding.btnSignin.isEnabled = true
                    }

                    userName =  binding.edtMobile.text.toString()

                    binding.edtMobile.background.setTint(
                        binding.edtMobile.resources.getColor(R.color.blueloginactive)
                    );
                } else {

                    binding.btnSignin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_grey_corners10))
                    binding.btnSignin.isEnabled = false

                    binding.edtMobile.background.setTint(
                        binding.edtMobile.resources.getColor(R.color.greygpay)
                    );
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.btnSignin.setOnClickListener {
            if (userName.equals("")) {
                binding.btnSignin.isEnabled = false
                return@setOnClickListener
            }

            if (!binding.btnSignin.isEnabled) {
                return@setOnClickListener
            }

            loginScreenSubmitTracking(MainApp.instance.uniquePsuedoID)

            if (IsInternetAvailable(this)) {
                presenter.validateLogin(userName)
            }
        }
        binding.actionToContactUs.setOnClickListener {
            val intent = Intent(this, ContactusActivity::class.java)
            startActivity(intent)
        }

        binding.actionToForgetpin.setOnClickListener {
            val intent = Intent(this, ForgetPinActivity::class.java)
            startActivity(intent)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegistMerchantActivity::class.java)
            startActivity(intent)
        }

        binding.versiapps.text = BuildConfig.VERSION_NAME

        checkAndRequestPermissions()
        loginScreenOpenedTracking(MainApp.instance.uniquePsuedoID)
    }

    //Multiple Permissions
    private fun checkAndRequestPermissions(): Boolean {
        val permissionReadExternalStorage: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) else ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listPermissionsNeeded.add(
                Manifest.permission.READ_MEDIA_IMAGES
            ) else listPermissionsNeeded.add(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                var necessaryPermissionString = resources.getString(R.string.str_necessary_permission)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    perms[Manifest.permission.READ_MEDIA_IMAGES] = PackageManager.PERMISSION_GRANTED
                    perms[Manifest.permission.POST_NOTIFICATIONS] =
                        PackageManager.PERMISSION_GRANTED
                } else {
                    perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                        PackageManager.PERMISSION_GRANTED
                    perms[Manifest.permission.READ_EXTERNAL_STORAGE] =
                        PackageManager.PERMISSION_GRANTED
                }

                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (perms[Manifest.permission.READ_MEDIA_IMAGES] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.POST_NOTIFICATIONS] == PackageManager.PERMISSION_GRANTED) {
                            return
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this,
                                    Manifest.permission.READ_MEDIA_IMAGES
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    this,
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            ) {
                                showDialogOK(
                                    necessaryPermissionString
                                ) { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            Toast.makeText(
                                                this,
                                                necessaryPermissionString,
                                                Toast.LENGTH_LONG
                                            ).show()
                                    }
                                }
                            } else {
                                permissionSettingScreen()
                            }
                        }
                    } else {
                        if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            ) {
                                showDialogOK(
                                    necessaryPermissionString
                                ) { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            Toast.makeText(
                                                this,
                                                necessaryPermissionString,
                                                Toast.LENGTH_LONG
                                            ).show()
                                    }
                                }
                            } else {
                                permissionSettingScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun permissionSettingScreen() {
        var enablePermissionString = resources.getString(R.string.str_enable_permission)
        Toast.makeText(this, enablePermissionString, Toast.LENGTH_LONG)
            .show()
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
        finish()
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        var cancel = resources.getString(R.string.txt_title_konfirmasi_cancel)
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton(cancel, okListener)
            .create()
            .show()
    }

    companion object {
        var startTime: Long? = null
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun init() {
        MainApp.instance!!.appComponent!!.inject(this)
        presenter = LoginPresenter(service, this)
    }

    fun startTimer() {
        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        startTime = cal.timeInMillis
    }

    override fun onErrorMessage(message: String) {
        if (message.equals("SQLSTATE[22P02]: Invalid text representation: 7 ERROR:  invalid input syntax for integer: \"\"")) {
            currentMessage = resources.getString(R.string.str_not_regis)
        } else {
            currentMessage = message
        }
        popupGagallogin(currentMessage)
        //loginErrorTracker()
    }

    override fun onErrorToken(message: String) {
        if (message.equals("SQLSTATE[22P02]: Invalid text representation: 7 ERROR:  invalid input syntax for integer: \"\"")) {
            currentMessage = resources.getString(R.string.str_not_regis)
        } else {
            currentMessage = message
        }

        popupGagallogin(currentMessage)
    }

    override fun showLoading() {
        showLoading(true)
    }

    override fun hideLoading() {
        showLoading(false)
    }

    override fun onSuccessValidateLogin(response: ResponseLogin) {
        MainApp.instance.sharedPreferences?.edit()
            ?.putString(SharedPreferencesUtils._KEY_USERNAME, userName)?.commit()
        if (response.data?.flag_create_password == true) {
            val intent = Intent(this, CreatePinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginPinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun doRequestPermissionCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    private fun loginScreenTracking(mobileNumber: String) {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number),
            userName
        )
        val bundle = Bundle()
        bundle.putBoolean(getString(R.string.gpay_user), true)
        MainApp.firebaseAnalytics.logEvent(getString(R.string.loginscreen_viewed), bundle)
    }

    private fun loginScreenOpenedTracking(deviceId: String){
        val bundle = Bundle()
        bundle.putString(getString(R.string.app_opened),deviceId)
        MainApp.firebaseAnalytics.logEvent(getString(R.string.loginscreen_opened), bundle)
    }

    private fun loginScreenSubmitTracking(deviceId: String){
        val bundle = Bundle()
        bundle.putString(getString(R.string.login_submit),deviceId)
        MainApp.firebaseAnalytics.logEvent(getString(R.string.loginscreen_submit), bundle)
    }
}
