package com.gpay.merchantapp.pages.home.transhistory

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityTransactionBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseMsStd
import com.gpay.merchantapp.network.response.ResponseProfile
import com.gpay.merchantapp.network.response.ResponseTransactionList
import com.gpay.merchantapp.utils.*
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TransHistoryActivity : BaseActivity(), TransHistoryView {

    var transDateFrom = ""
    var transDateTo = ""

    @Inject
    lateinit var service: Service
    lateinit var presenter: TransHistoryPresenter

    lateinit var adapter: TransHistoryAdapter
    val items = ArrayList<ResponseTransactionList.Datum>()
    var counter = 1
    var jmlData = 0
    var qtyData = 0
    var pageSize = 100000

    private var WIDTH_QR: Int = 0
    private var HEIGHT_QR: Int = 0

    var sdfTrxHistory = SimpleDateFormat("dd MMM yyyy") //For Trans History
    var sendDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        historyViewedTracking()

        init()

        if (intent.extras != null) {
            if (intent.extras!!.getString("tipe_history", "").equals("settle")) {

                var settleDate = intent.extras?.getString("settlement_date", "")
                counter = 1
                if (settleDate != null) {

                    binding.dateFrom.text = settleDate
                    binding.dateTo.text = settleDate

                    var settledDate = sdfTrxHistory.parse(settleDate)

                    settleDate = sendDateFormat.format(settledDate)

                    presenter.transactionList(settleDate, settleDate, "1", pageSize.toString())
                }
            }
        }

        binding.imgDropArrow1.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerFragment = DatePickerFragment(year, month, day)
            datePickerFragment.setListener { format, format2 ->
                binding.dateFrom.setText(format)
                transDateFrom = format2

                if (transDateTo.equals("")) {

                } else if (transDateFrom > transDateTo) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalbesar))
                } else {
                    counter = 1
                    presenter.transactionList(transDateFrom, transDateTo, counter.toString(), pageSize.toString())
                }
            }
            datePickerFragment.show(supportFragmentManager, "Date From")
        }

        binding.imgDropArrow2.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerFragment = DatePickerFragment(year, month, day)
            datePickerFragment.setListener { format, format2 ->
                binding.dateTo.setText(format)
                transDateTo = format2

                if (transDateFrom.equals("")) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalkosong))
                } else if (transDateFrom > transDateTo) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalbesar))
                } else {
                    counter = 1
                    presenter.transactionList(transDateFrom, transDateTo, counter.toString(), pageSize.toString())
                }
            }
            datePickerFragment.show(supportFragmentManager, "Date To")
        }

        binding.dateFrom.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerFragment = DatePickerFragment(year, month, day)
            datePickerFragment.setListener { format, format2 ->
                binding.dateFrom.text = format
                transDateFrom = format2

                if (transDateTo.equals("")) {

                } else if (transDateFrom > transDateTo) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalbesar))
                    binding.dateFrom.text = ""
                } else {
                    counter = 1
                    presenter.transactionList(transDateFrom, transDateTo, counter.toString(), pageSize.toString())
                }
            }
            datePickerFragment.show(supportFragmentManager, "Date From")
        }

        binding.dateTo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerFragment = DatePickerFragment(year, month, day)
            datePickerFragment.setListener { format, format2 ->
                binding.dateTo.text = format
                transDateTo = format2

                if (transDateFrom.equals("")) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalkosong))
                } else if (transDateFrom > transDateTo) {
                    popupGagallogin(resources.getString(R.string.str_transaction_validasi_tglawalbesar))
                    binding.dateTo.text = ""
                } else {
                    counter = 1
                    presenter.transactionList(transDateFrom, transDateTo, counter.toString(), pageSize.toString())
                }
            }
            datePickerFragment.show(supportFragmentManager, "Date To")
        }

        binding.actionBack.setOnClickListener {
            historyCloseracking()
            finish()
        }
    }

    fun init() {
        MainApp.instance.appComponent?.inject(this);
        presenter = TransHistoryPresenter(service, this)


        binding.linearlayoutTrxHistory.visibility = View.VISIBLE
        binding.linearlayoutTrxNoData.visibility = View.GONE

        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerTrans.layoutManager = layoutManager
        adapter = TransHistoryAdapter(this)
        binding.recyclerTrans.adapter = adapter

       /* recyclerTrans.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                presenter.transactionListMore(
                    transDateFrom,
                    transDateTo,
                    counter.toString(),
                    pageSize.toString()
                )
            }
        })*/

        binding.recyclerTrans.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                binding.recyclerTrans,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        showBottomSheetDialog(items[position])
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        return
                    }
                })
        )
    }

    fun showBottomSheetDialog(data: ResponseTransactionList.Datum) {
        val myImage: ImageView
        val buttonClose: ImageView
        val dateTranHistory: TextView
        val transDesc: TextView
        val customerPhone: TextView
        val refId: TextView
        val amount: TextView
        val issuer: TextView
        val transType: TextView
        var QR = ""
        var cekPhone = ""

        val view = layoutInflater.inflate(R.layout.activity_bottomsheet_transhistory, null)
        val dialog = BottomSheetDialog(this)

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setContentView(view, params)

        myImage = view.findViewById<View>(R.id.imgBarcode) as ImageView
        buttonClose = dialog.findViewById<View>(R.id.action_close) as ImageView
        dateTranHistory = view.findViewById<View>(R.id.txt_date_transhistory) as TextView
        transDesc = view.findViewById<View>(R.id.txt_desc_transhistory) as TextView
        customerPhone = view.findViewById<View>(R.id.txt_cust_transhistory) as TextView
        refId = view.findViewById<View>(R.id.txt_refid) as TextView
        amount = view.findViewById<View>(R.id.txt_amount) as TextView
        issuer = view.findViewById<View>(R.id.txt_issuer_name) as TextView
        transType = view.findViewById<View>(R.id.txt_trans_type) as TextView

        buttonClose.setOnClickListener() {
            dialog?.dismiss()
        }

        WIDTH_QR = 580
        HEIGHT_QR = 580
        var multiFormatWriter = MultiFormatWriter()

        if (data.reference_number == null) {
            QR = ""
        } else {
            QR = data.reference_number!!
        }

        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
        var simpleDateFormat2 = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        var date = simpleDateFormat.parse(data.transaction_time)
        val date2 = simpleDateFormat2.format(date)

        dateTranHistory.text = date2

        if (data.tran_type != null) {
            transDesc.text = data.tran_type!!
        }

        if (data.mobile_phone_no != null) {
            cekPhone = data.mobile_phone_no!!
        }

        transType.text = data.transaction_type

        if (data.transaction_type!!.toUpperCase().contains("off us".toUpperCase())) {

            if (data.source_of_fund != null && data.source_of_fund != "") {
                issuer.text = data.source_of_fund!!.toLowerCase().capitalize()
            }
        } else {
            issuer.text = "Gaja"
        }

        if (cekPhone != null) {
            if (cekPhone.equals("")) {

            } else {

                if (!cekPhone.get(0).toString().equals("0")) {
                    customerPhone.text = "0" + data.mobile_phone_no!!
                } else {
                    customerPhone.text = data.mobile_phone_no!!
                }
            }
        }

        if (data.reference_number != null) {
            refId.text = data.reference_number!!
        }

        amount.text = "Rp " + data.amount?.let { ConvertData().convertCurency(it).get("format") }

        if (!QR.equals("")) {
            try {
                var bitMatrix =
                    multiFormatWriter.encode(QR, BarcodeFormat.CODE_128, WIDTH_QR, HEIGHT_QR)
                var barcodeEncoder = BarcodeEncoder()
                var bitmap = barcodeEncoder.createBitmap(bitMatrix)

                myImage.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace();
            }
        }

        dialog.show()
    }

    override fun onBackPressed() {
        historyCloseracking()
        finish()
    }

    override fun onErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        showDialog(true)
    }

    override fun hideLoading() {
        showDialog(false)
    }

    override fun onSuccesListTransaction(response: ResponseTransactionList) {
        if (response.data!!.isNotEmpty()) {

            if (items.isNotEmpty()) {
                items.clear()
                 jmlData = 0
            }

            for (i in response.data!!.indices) {
                items.add(i, response.data!![i])
                jmlData += (response.data!![i].amount?.toInt() ?: 0)
            }


            binding.txtCountTrx.text = ConvertData().convertCurency(qtyData.toString()).get("format")
            binding.txtTransAmount.text =
                "Rp " + ConvertData().convertCurency(jmlData.toString()).get("format")

            adapter.notifyDataSetChanged()

            counter += 1;

            binding.linearlayoutTrxHistory.visibility = View.VISIBLE
            binding.linearlayoutTrxNoData.visibility = View.GONE

            items.sortWith(Comparator { lhs, rhs ->
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs.transaction_time!! > rhs.transaction_time.toString()) -1 else if (lhs.transaction_time!! < rhs.transaction_time.toString()) 1 else 0
            })


            adapter.addData(items)

        } else {

            binding.linearlayoutTrxHistory.visibility = View.GONE
            binding.linearlayoutTrxNoData.visibility = View.VISIBLE
            binding.txtTransAmount.text = "Rp 0"
        }
    }

    override fun onSuccesListTransactionMore(response: ResponseTransactionList) {
        if (response.data!!.isNotEmpty()) {

            for (i in response.data!!.indices) {
                items.add(i, response.data!![i])
            }

            adapter.notifyDataSetChanged()

            counter += 1;

            items.sortWith(Comparator { lhs, rhs ->
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs.transaction_time!! > rhs.transaction_time.toString()) -1 else if (lhs.transaction_time!! < rhs.transaction_time.toString()) 1 else 0
            })

            adapter.addData(items)
        }
    }

    override fun onSuccesShowProfile(response: ResponseProfile) {
        TODO("Not yet implemented")
    }

    override fun onSuccessMsStd(response: ResponseMsStd, id: String) {
        TODO("Not yet implemented")
    }

    private fun historyViewedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.trxhistory_viewed), bundle)
    }

    private fun historyCloseracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number), "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER, ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.trxhistory_close_tapped), bundle)
    }
}