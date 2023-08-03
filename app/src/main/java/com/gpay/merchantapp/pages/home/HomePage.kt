package com.gpay.merchantapp.pages.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.gpay.merchantapp.MainActivity
import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ActivityHomeBinding
import com.gpay.merchantapp.network.Service
import com.gpay.merchantapp.network.response.ResponseProfile
import com.gpay.merchantapp.network.response.ResponseQRDownload
import com.gpay.merchantapp.network.response.ResponseTransactionList
import com.gpay.merchantapp.pages.home.transhistory.TransHistoryActivity
import com.gpay.merchantapp.pages.home.transhistory.TransHistoryAdapter
import com.gpay.merchantapp.utils.*
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class HomePage : BaseFragment(), HomeView {
    @Inject
    lateinit var service: Service
    lateinit var presenter: HomePresenter
    lateinit var adapter: TransHistoryAdapter
    lateinit var format_yyyyyMMdd: String
    lateinit var broadcastReceiver: BroadcastReceiver

    val items = ArrayList<ResponseTransactionList.Datum>()
    var counter = 1
    var refreshAble = true;
    var refresh = false;
    var jmlData = 0
    var pageSize = 1000000

    private var WIDTH_QR: Int = 0
    private var HEIGHT_QR: Int = 0

    var c: Calendar? = null

    //lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): HomePage {
            return HomePage()
        }
        var startTime: Long? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        presenter.getProfile()
        presenter.getQRDownloadLink()
        loadDataTransaction()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                loadDataTransaction()
            }
        }
    }

    override fun onStart() {
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, IntentFilter(Global.NOTIF_TRANSACTION))
        super.onStart()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        var isSideMenu = MainApp?.instance?.sharedPreferences?.getBoolean(
            SharedPreferencesUtils._KEY_SIDE_MENU,
            false
        )

        if (items.isNullOrEmpty()) {
            if (isSideMenu == false) {
                loadDataTransaction()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
        //val view: View = inflater.inflate(R.layout.activity_home, container, false)
        //swipeRefreshLayout = view.findViewById(R.id.swipe)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var parentActivity = activity as MainActivity

        /*home_receive_payment.setOnClickListener {
            *//*receivePaymentTappedTracking()
            val intent = Intent(context, ReceivePaymentActivity::class.java)
            startActivity(intent)*//*

            val intent = Intent(context, SettlementHistoryActivity::class.java)
            intent.putExtra("tipe_history", "Settlement")
            startActivity(intent)
        }*/

        binding.homeTranshistory.setOnClickListener {
            transHistoryTappedTracking()
            val intent = Intent(context, TransHistoryActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.recyclerTrans.layoutManager = layoutManager
        adapter = TransHistoryAdapter(activity)
        binding.recyclerTrans.adapter = adapter

        /* recyclerTrans.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
             override fun onLoadMore(current_page: Int) {
                 refresh = false
                 presenter.transactionListMore(

                     format_yyyyyMMdd,
                     format_yyyyyMMdd,
                     counter.toString(),
                     pageSize.toString()
                 )
             }
         })*/

        binding.recyclerTrans.addOnItemTouchListener(
            RecyclerItemClickListener(
                this.requireContext(),
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

        binding.swipe.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipe.isRefreshing = false
                loadDataTransaction()
            }, 300)
        }
    }

    fun loadDataTransaction() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        format_yyyyyMMdd = sdf.format(c!!.time)
        presenter.transactionList(format_yyyyyMMdd, format_yyyyyMMdd, "1", pageSize.toString())
    }

    fun showBottomSheetDialog(data: ResponseTransactionList.Datum) {
        val myImage: ImageView
        val buttonClose: ImageView
        val dateTranHistory: TextView
        val transDesc: TextView
        val customerPhone: TextView
        val refId: TextView
        val amount: TextView
        var QR = ""
        var cekPhone = ""
        val issuerName: TextView

        val view = layoutInflater.inflate(R.layout.activity_bottomsheet_transhistory, null)
        val dialog = BottomSheetDialog(this.requireContext())
        //val heightInPixels = ScreenUtils(this.context!!).height
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
        issuerName = view.findViewById<View>(R.id.txt_issuer_name) as TextView

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

        if (data.transaction_type!!.toUpperCase().contains("off us".toUpperCase())) {

            if (data.source_of_fund != null && data.source_of_fund != "") {
                issuerName.text = data.source_of_fund!!.toLowerCase().capitalize()
            }
        } else {
            issuerName.text = "Gaja"
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

    fun init() {
        MainApp.instance!!.appComponent!!.inject(this)
        presenter = HomePresenter(service, this)

        c = Calendar.getInstance()
    }

    override fun onErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        showDialog(true)
    }

    override fun hideLoading() {
        showDialog(false)
    }

    override fun onSuccesShowProfile(response: ResponseProfile) {

        MainApp.instance.sharedPreferences!!.edit()
            .putString(SharedPreferencesUtils._KEY_MERCHANT, response.data!!.name)
            .commit()

        binding.txtHomepageStorename.text = response.data?.name
        binding.txtHomepageAddress.text = response.data?.address
        binding.txtHomepageEmail.text = response.data?.email

        val cekPhone = response.data?.phone1

        if (cekPhone != null && cekPhone != "") {
            if (cekPhone[0].toString() != "0") {
                binding.txtHomepagePhone.text = "0" + response.data?.phone1
            } else {
                binding.txtHomepagePhone.text = response.data?.phone1
            }
        }

        var parentActivity = activity as MainActivity
        parentActivity.setSideMenu(
            "" + response.data?.store_id,
            "" + response.data?.merchant_id,
            "" + response.data?.name,
            "" + response.data?.name,
            "" + response.data?.address,
            "" + response.data?.address,
            "" + response.data?.last_login_date
        )
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

            binding.txtTransAmount.text =
                "Rp " + ConvertData().convertCurency(jmlData.toString()).get("format")

            counter += 1;

            binding.linearlayoutHistory.visibility = View.VISIBLE
            binding.linearlayoutNoData.visibility = View.GONE

            items.sortWith(Comparator { lhs, rhs ->
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs.transaction_time!! > rhs.transaction_time.toString()) -1 else if (lhs.transaction_time!! < rhs.transaction_time.toString()) 1 else 0
            })

            adapter.addData(items)
            adapter.notifyDataSetChanged()

            if (binding.txtHomepageStorename.text == "") {
                presenter.getProfile()
            }

        } else {
            if (binding.txtHomepageStorename.text == "") {
                presenter.getProfile()
            }

            binding.linearlayoutHistory.visibility = View.GONE
            binding.linearlayoutNoData.visibility = View.VISIBLE

            binding.txtTransAmount.text = "Rp 0"
        }
    }

    override fun onSuccesListTransactionMore(response: ResponseTransactionList) {

        refreshAble = false

        if (refresh)
            return

        if (response.data!!.isNotEmpty()) {

            for (i in response.data!!.indices) {
                items.add(i, response.data!![i])
            }

            counter += 1;

            items.sortWith(Comparator { lhs, rhs ->
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs.transaction_time!! > rhs.transaction_time.toString()) -1 else if (lhs.transaction_time!! < rhs.transaction_time.toString()) 1 else 0
            })

            adapter.addData(items)
            //adapter.notifyDataSetChanged()

            if (binding.txtHomepageStorename.text == "") {
                presenter.getProfile()
            }

            Timer("refreshAble", false).schedule(500) {
                refreshAble = true
            }
        }
    }

    override fun onSuccesQRDownload(response: ResponseQRDownload) {
        if (response !=null) {
            var qris = response.data?.downloadLink;

            MainApp.instance.sharedPreferences?.edit()
                ?.putString(SharedPreferencesUtils._KEY_QRIS, qris)?.commit()
        }
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

    private fun homeRefreshTracking() {
        val bundle = Bundle()
        bundle.putInt(getString(R.string.number_of_refresh), 1)
        MainApp.firebaseAnalytics.logEvent(getString(R.string.homescreen_refreshed), bundle)
    }

    private fun scanTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number),
            "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER,
                ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.scanqr_tapped), bundle)
    }

    private fun qrTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number),
            "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER,
                ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.showqr_tapped), bundle)
    }

    private fun historyTappedTracking() {
        MainApp.firebaseAnalytics.setUserProperty(
            getString(R.string.mobile_number),
            "62" + MainApp.instance.sharedPreferences?.getString(
                SharedPreferencesUtils._KEY_MOBILE_NUMBER,
                ""
            )
        )
        val bundle = Bundle()
        MainApp.firebaseAnalytics.logEvent(getString(R.string.trxhistory_tapped), bundle)
    }

    fun startTimer() {
        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        startTime = cal.timeInMillis
    }

    var callback: OnIconRefreshClickListener? = null

    fun setOnIconRefreshClickListener(callback: OnIconRefreshClickListener?) {
        this.callback = callback
    }

    interface OnIconRefreshClickListener {
        fun refreshDataTrx()
    }
}