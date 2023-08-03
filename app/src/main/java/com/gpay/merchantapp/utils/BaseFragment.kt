package com.gpay.merchantapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.Fragment
import com.gpay.merchantapp.R

open class BaseFragment : Fragment() {
    lateinit var builder: AlertDialog.Builder
    internal var pd: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        builder = Builder(activity!!)
        pd = LoadingDialog(context!!)
        pd!!.setCancelable(false)
    }

    fun showDialog(show: Boolean) {
        if (null != pd)
            if (show)
                pd!!.show()
            else
                pd!!.dismiss()
    }

    fun showMessage(message: String) {
        builder.setMessage(message)
            .setTitle(R.string.app_name)
            .setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    /*protected fun showBottomSheetDialog(myLayoutRes: Int) {
        val view = layoutInflater.inflate(myLayoutRes, null)
        val dialog = activity?.let { BottomSheetDialog(it) }

        val heightInPixels = activity?.let { ScreenUtils(it).height }
        val params = (heightInPixels?.times(
            0.55
        ))?.toInt()?.let {
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, it
            )
        }
        dialog?.setContentView(view, params)

        var buttonClose: ImageView
        if (dialog != null) {
            buttonClose = dialog.findViewById<View>(R.id.action_close) as ImageView

            buttonClose.setOnClickListener(){
                dialog?.dismiss()
            }
        }



        dialog?.show()
    }*/

    protected fun IsInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
