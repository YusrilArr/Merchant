package com.gpay.merchantapp.pages.home.receivepayment

import ScreenUtils
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityReceivepaymentBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseShowQr
import com.gpay.merchantapp.utils.BaseActivity
import com.gpay.merchantapp.utils.ConvertData
import com.gpay.merchantapp.utils.ThousandSeparatorTextWatcher
import com.journeyapps.barcodescanner.BarcodeEncoder
import javax.inject.Inject

class ReceivePaymentActivity : BaseActivity(), ReceivePaymentView {
    private var amount: String = ""

    @Inject
    lateinit var service: Service
    lateinit var presenter: ReceivePaymentPresenter

    private var WIDTH_QR: Int = 0
    private var HEIGHT_QR: Int = 0

    private lateinit var binding: ActivityReceivepaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceivepaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        binding.edtAmount.addTextChangedListener(ThousandSeparatorTextWatcher(binding.edtAmount))

        binding.edtAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                amount = binding.edtAmount.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.btnGenerateQR.setOnClickListener {
            if (IsInternetAvailable(this)) {
                if (!amount.equals("")) {
                    presenter.showQr(ThousandSeparatorTextWatcher.getOriginalString(amount))
                }
            }
        }

        binding.actionBack.setOnClickListener {
            finish()
        }

    }

    fun showBottomSheetDialog(data: String) {
        val myImage: ImageView
        val buttonClose: ImageView
        val txtamount: TextView
        val storename: TextView

        val view = layoutInflater.inflate(R.layout.activity_bottomsheet_qrcode, null)
        val dialog = BottomSheetDialog(this)

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setContentView(view, params)

        myImage = view.findViewById<View>(R.id.imageViewQrcode) as ImageView
        txtamount = view.findViewById<View>(R.id.txt_qr_amount) as TextView
        storename = view.findViewById<View>(R.id.txt_qr_merchant) as TextView
        buttonClose = dialog.findViewById<View>(R.id.action_close) as ImageView

        storename.text = MainApp.instance.sharedPreferences!!.getString("merchantname", "")
        txtamount.text = "Rp " + ConvertData().convertCurency(amount).get("format")

        buttonClose.setOnClickListener() {
            dialog?.dismiss()
        }

        WIDTH_QR = 580
        HEIGHT_QR = 580
        var multiFormatWriter = MultiFormatWriter()
        var QR = data

        try {
            var bitMatrix = multiFormatWriter.encode(QR, BarcodeFormat.QR_CODE, WIDTH_QR, HEIGHT_QR)
            var barcodeEncoder = BarcodeEncoder()
            var bitmap = barcodeEncoder.createBitmap(bitMatrix)

            myImage.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace();
        }

        dialog.show()
    }

    fun init() {
        MainApp.instance?.appComponent?.inject(this)
        presenter = ReceivePaymentPresenter(service, this)
    }

    override fun onErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        showLoading(true)
    }

    override fun hideLoading() {
        showLoading(false)
    }

    override fun onSuccessQrCode(response: ResponseShowQr) {
        showBottomSheetDialog(response.data?.qris_string!!)
    }
}