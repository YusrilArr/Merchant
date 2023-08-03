package com.gpay.merchantapp.utils

import ScreenUtils
import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gpay.merchantapp.R

open class BaseActivity : AppCompatActivity() {
    lateinit var fm: FragmentManager
    internal var pd: LoadingDialog? = null
    internal var pd1: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm = supportFragmentManager
        pd = LoadingDialog(this!!)
        pd1 = ProgressDialog(this, R.style.MyAlertDialogStyle)
        pd1!!.setCancelable(false)
        pd1!!.setMessage("Loading ...")
    }

    protected fun loadFragment(fragment: Fragment, baseframe: View) {
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.commit()
    }

    protected fun replaceFragment(fragment: Fragment, baseframe: View) {
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    protected fun replaceRemoveFragment(fragment: Fragment, baseframe: View) {
        fm.popBackStack()
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    fun showDialog(show: Boolean) {
        if (null != pd)
            if (show)
                pd!!.show()
            else
                pd!!.dismiss()
    }

    fun showLoading(show: Boolean) {
        if (null != pd1)
            if (show)
                pd1!!.show()
            else
                pd1!!.dismiss()
    }

    protected open fun showBottomSheetDialog(myLayoutRes: Int, data: String) {
        val view = layoutInflater.inflate(myLayoutRes, null)
        val dialog = BottomSheetDialog(this)

        val heightInPixels = ScreenUtils(this).height
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (heightInPixels*0.62).toInt())
        dialog.setContentView(view,params)

        val buttonClose: ImageView
        buttonClose = dialog.findViewById<View>(R.id.action_close) as ImageView

        buttonClose.setOnClickListener(){
            dialog?.dismiss()
        }

        dialog.show()
    }

    protected fun IsInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    protected fun popupGagallogin(message: String) {
        val buttonClose: TextView
        val txt_title_popup: TextView
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.pop_up_gagal_login)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        txt_title_popup = dialog.findViewById<View>(R.id.txt_title_popup) as TextView

        var messageFilter = message
        if(messageFilter.uppercase().contains("Mobile phone number not registered".uppercase())){
            messageFilter = "Username not registered"
        }else if (messageFilter.uppercase().contains("Nomor telepon tidak terdaftar".uppercase())) {
            messageFilter = "Username tidak terdaftar"
        }else if (messageFilter.uppercase().contains("Invalid username or PIN. Please try again".uppercase())) {
            messageFilter = "Invalid PIN. Please try again"
        }

        txt_title_popup.text = messageFilter
        buttonClose = dialog.findViewById<View>(R.id.action_close) as TextView

        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    protected open fun doRequestPermissionReadWriteStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 113)
            }
        }
    }

    protected fun openDialer() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + "(021)2525971")
        startActivity(intent)
    }
    protected fun openEmail(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("helpdesk.ops@gpay.id"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }else{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("helpdesk.ops@gpay.id"))
            intent.setPackage("com.google.android.gm")
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
            else
                Toast.makeText(this, "Gmail App is not installed", Toast.LENGTH_SHORT).show()
        }

    }
}