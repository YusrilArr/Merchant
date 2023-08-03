package com.gpay.merchantapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.gpay.merchantapp.databinding.ActivityMainBinding
import com.gpay.merchantapp.pages.changepin.ChangePinActivity
import com.gpay.merchantapp.pages.contactus.ContactusActivity
import com.gpay.merchantapp.pages.home.HomePage
import com.gpay.merchantapp.pages.home.receivepayment.ReceivePaymentActivity
import com.gpay.merchantapp.pages.home.settlementhistory.SettlementHistoryActivity
import com.gpay.merchantapp.pages.home.transhistory.TransHistoryActivity
import com.gpay.merchantapp.pages.login.LoginActivity
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.FileDownloader.downloadFile
import com.gpay.merchantapp.utils.SharedPreferencesUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomePage.OnIconRefreshClickListener {

    lateinit var iconRefresh: ImageView
    lateinit var homeFragment: HomePage

    companion object {
        var startTime: Long? = null
    }

    private var qrisPath: String = ""
    private var pdfPath: String = "https://gaja.id/gajamerchant/merchant-book.pdf"
    private var url: URL? = null
    private var filename: String? = null
    private var mimeType: String? = null
    private var downloadFileType: Int = 0

    var CREATE_FILE = false

    private lateinit var binding: ActivityMainBinding

    private lateinit var txtStoreName:TextView
    private lateinit var txtLastLogin:TextView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        iconRefresh = findViewById(R.id.icon_refresh)
        val tvTitle:AppCompatTextView = findViewById(R.id.tvTitle)
        val frameMainActivity:FrameLayout = findViewById(R.id.frame_mainactivity)
        val navReceivePayment:LinearLayout = findViewById(R.id.nav_receive_payment)
        val navTransHistory:LinearLayout = findViewById(R.id.nav_trans_history)
        val navSettlementHistory:LinearLayout = findViewById(R.id.nav_settlement_history)
        val navChangePin:LinearLayout = findViewById(R.id.nav_change_pin)
        val navContactUs:LinearLayout = findViewById(R.id.nav_contactus)
        val navDownloadGuide:LinearLayout = findViewById(R.id.nav_download_guide)
        val navDownloadQris:LinearLayout = findViewById(R.id.nav_download_qris)
        val navSignOut:Button = findViewById(R.id.nav_btn_signout)
        val txtVersiApps:TextView = findViewById(R.id.versiapps)
        txtStoreName = findViewById(R.id.txt_storename)
        txtLastLogin = findViewById(R.id.txt_lastlogin)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        var drawerListener = CustomDrawer()
        binding.drawerLayout.addDrawerListener(drawerListener)
        toggle.syncState()

        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.burger_menu);

        val homePage = HomePage.newInstance()
        var extras = intent.extras

        if (extras != null) {
            var flagPass = extras.getBoolean("flag_password")
            var bundle = Bundle()
            bundle.putBoolean("flag_password", flagPass)
            homePage.arguments = bundle
        }


        loadFragment(homePage, frameMainActivity)

        binding.navView.setCheckedItem(R.id.nav_home)
        binding.navView.setNavigationItemSelectedListener(this)

        tvTitle.text = ""

        navReceivePayment.setOnClickListener {
            receivePaymentTappedTracking()
            val intent = Intent(applicationContext, ReceivePaymentActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        navTransHistory.setOnClickListener {
            transHistoryTappedTracking()
            val intent = Intent(applicationContext, TransHistoryActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        navSettlementHistory.setOnClickListener {
            transHistoryTappedTracking()
            val intent = Intent(applicationContext, SettlementHistoryActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        navChangePin.setOnClickListener {
            changePinTappedTracking()
            val intent = Intent(applicationContext, ChangePinActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        navContactUs.setOnClickListener {
            contactTappedTracking()
            val intent = Intent(applicationContext, ContactusActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        navDownloadGuide.setOnClickListener {
            popupDownloadFile(0)
        }

        navDownloadQris.setOnClickListener {
            popupDownloadFile(1)
        }

        navSignOut.setOnClickListener {
            logoutTappedTracking()
            MainApp.instance.sharedPreferences?.edit()?.clear()?.commit()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            binding.drawerLayout.closeDrawer(Gravity.START)
        }

        txtVersiApps.text = BuildConfig.VERSION_NAME

        iconRefresh.setOnClickListener {
            refreshDataTrx()
        }
    }

    override fun onResume() {
        super.onResume()

        if (CREATE_FILE) {
            val t = Toast.makeText(
                applicationContext,
                resources.getString(R.string.str_downloaded_guide_book),
                Toast.LENGTH_LONG
            )
            t.show()
        }
    }

    private fun popupDownloadFile(fileType: Int) {
        downloadFileType = fileType
        val buttonDownload: TextView
        val buttonQuit: TextView
        val title:TextView
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_back_on_downloadpdf)

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonDownload = dialog.findViewById<View>(R.id.action_download) as TextView
        buttonQuit = dialog.findViewById<View>(R.id.action_quit) as TextView
        title = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        if (fileType == 0){
            title.text = resources.getString(R.string.str_popup_download_pdf);
        }else{
            title.text = resources.getString(R.string.str_popup_download_qris);
        }

        buttonDownload.setOnClickListener {
            doDownloadFile(fileType)//Using Download Manager
            //saveFileToStorageIntent() // User Select Download Location
            //downloadPdfWithMediaStore() // Using Media Store (Directly Save to Download Directory)
            dialog.dismiss()
        }

        buttonQuit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun popupGagalDownloadQRIS() {
        val buttonQuit: TextView
        val title:TextView
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_gagal_login)

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonQuit = dialog.findViewById<View>(R.id.action_close) as TextView
        title = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        title.text = resources.getString(R.string.str_popup_failed_qris);

        buttonQuit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadPdfWithMediaStore() {
        GlobalScope.launch {
            try {
                val url =
                    URL(pdfPath)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.doOutput = true
                connection.connect()
                val pdfInputStream: InputStream = connection.inputStream

                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "merchant-book")
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = contentResolver

                val collection =
                    MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val itemUri = resolver.insert(collection, values)

                if (itemUri != null) {
                    resolver.openFileDescriptor(itemUri, "w").use { parcelFileDescriptor ->
                        ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor)
                            .write(pdfInputStream.readBytes())
                    }
                    values.clear()
                    values.put(MediaStore.Downloads.IS_PENDING, 0)
                    resolver.update(itemUri, values, null, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveFileToStorageIntent() {
        CREATE_FILE = false
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        intent.putExtra(Intent.EXTRA_TITLE, "merchant-book.pdf")
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            CREATE_FILE = true
            GlobalScope.launch {
                val request = Request.Builder()
                    .url(pdfPath)
                    .build()
                val client = OkHttpClient.Builder()
                    .build()

                val response = client.newCall(request).execute()

                if (resultCode == RESULT_OK && data != null) {
                    writePDFToFile(data.data, response.body()!!)
                }
            }
        }
    }

    private fun writePDFToFile(uri: Uri?, body: ResponseBody) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileSize = body.contentLength()
            val fileReader = ByteArray(fileSize.toInt())
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = contentResolver.openOutputStream(uri!!)
            while (true) {
                val read: Int = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream?.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
            }
            outputStream?.flush()

        } catch (e: Exception) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun doDownloadFile(fileType: Int) {
        MainApp.instance.sharedPreferences?.edit()
            ?.putBoolean(SharedPreferencesUtils._KEY_SIDE_MENU, true)?.commit()

        try {
            if (fileType == 0) {
                url = URL(pdfPath)
                filename = "merchant-book.pdf"
                mimeType = "application/pdf"
            } else {
                qrisPath =
                    MainApp.instance.sharedPreferences?.getString(
                        SharedPreferencesUtils._KEY_QRIS,
                        ""
                    ).toString()
                url = URL(qrisPath)
                filename = "qris.png"
                mimeType = "image/png"
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        if (url == null || url!!.equals("")) {
            popupGagalDownloadQRIS()
        } else {

            val request = DownloadManager.Request(Uri.parse(url.toString() + ""))
            request.setTitle(filename)
            request.setMimeType(mimeType)
            request.allowScanningByMediaScanner()
            request.setAllowedOverMetered(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)

            val t = Toast.makeText(
                applicationContext,
                resources.getString(R.string.str_downloaded_file),
                Toast.LENGTH_LONG
            )
            t.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            112 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }

                    doDownloadFile(downloadFileType)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.str_write_storage_access),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val snack = Snackbar.make(
                        binding.navView,
                        resources.getString(R.string.str_popup_force_permission),
                        Snackbar.LENGTH_LONG
                    )
                    snack.setAction("NEXT", View.OnClickListener {
                        // executed when DISMISS is clicked
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        this.startActivity(intent)
                    })
                    snack.duration = 5000
                    snack.show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun download() {
        DownloadFile().execute(
            "https://gaja.id/gajamerchant/merchant-book.pdf",
            "merchant-book.pdf"
        )

        val t = Toast.makeText(
            applicationContext,
            resources.getString(R.string.str_downloaded_file),
            Toast.LENGTH_LONG
        )
        t.show()
    }

    private class DownloadFile() : AsyncTask<String?, Void?, Void?>() {

        override fun doInBackground(vararg params: String?): Void? {
            val fileUrl = "https://gaja.id/gajamerchant/merchant-book.pdf"
            val fileName = "merchant-book.pdf"// -> maven.pdf
            val folder: File =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val pdfFile = File(folder, fileName)

            try {
                pdfFile.createNewFile()

            } catch (e: IOException) {
                e.printStackTrace()

            }
            downloadFile(fileUrl, pdfFile)
            return null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CREATE_FILE = false
        closeAppTracking()
    }

    override fun onPause() {
        CREATE_FILE = false
        super.onPause()
    }

    fun closeAppTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number),
            "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER,
                ""
            )
        )
        val bundle = Bundle()
        bundle.putBoolean(getString(R.string.gpay_user), true)
        MainApp.firebaseAnalytics.logEvent(getString(R.string.close_app), bundle)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setSideMenu(
        storeid: String,
        merchantid: String,
        storename: String,
        merchantname: String,
        storeaddress: String,
        merchantaddres: String,
        lastLogin: String
    ) {

        if (storeid.equals("")) {
            txtStoreName.text = merchantname
        } else {
            txtStoreName.text = storename
        }

        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
        var simpleDateFormat2 = SimpleDateFormat("dd/MMM/yyyy")
        var date = simpleDateFormat.parse(lastLogin)
        var lastLogin2 = simpleDateFormat2.format(date)
        //txtLastLogin.text = lastLogin2.trim()
        txtLastLogin.text = "08/02/2023"
    }

    private fun receivePaymentTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.receive_payment_tapped), bundle)
    }

    private fun transHistoryTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.trans_history_tapped), bundle)
    }

    private fun changePinTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.change_pin_tapped), bundle)
    }

    private fun contactTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.contactus_tapped), bundle)
    }

    private fun logoutTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.logout_tapped), bundle)
    }

    private fun helpTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.help_tapped), bundle)
    }

    private fun termsTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.termsconditions_tapped), bundle)
    }

    fun startTimer() {
        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        startTime = cal.timeInMillis
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is HomePage)
            homeFragment = fragment as HomePage
        homeFragment.setOnIconRefreshClickListener(this)
    }

    override fun refreshDataTrx() {
        homeFragment.loadDataTransaction()
    }
}

class CustomDrawer : DrawerLayout.DrawerListener {
    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerClosed(drawerView: View) {
        MainApp.instance.sharedPreferences?.edit()
            ?.putBoolean(SharedPreferencesUtils._KEY_SIDE_MENU, false)?.commit()
    }

    override fun onDrawerOpened(drawerView: View) {
        MainApp.instance.sharedPreferences?.edit()
            ?.putBoolean(SharedPreferencesUtils._KEY_SIDE_MENU, true)?.commit()
    }
}



