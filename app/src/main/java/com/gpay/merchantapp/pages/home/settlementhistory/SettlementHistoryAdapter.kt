package com.gpay.merchantapp.pages.home.settlementhistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ItemSettlementHistoryBinding
import com.gpay.merchantapp.network.response.ResponseSettlementList
import com.gpay.merchantapp.pages.home.transhistory.TransHistoryActivity
import com.gpay.merchantapp.utils.ConvertData
import java.text.SimpleDateFormat
import java.util.*

class SettlementHistoryAdapter(var context: Context? = null) :
    RecyclerView.Adapter<SettlementHistoryAdapter.ViewHolderTransaction>() {
    private val items: ArrayList<ResponseSettlementList.Datum> = ArrayList()

    private val arrTranstype = arrayOf("Top up")
    private val arrTransdesc = arrayOf("GPay")
    var dateTrxHistory = ""
    var date: Date? = null
    var date2 = ""
    var date3 = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolderTransaction {
        val binding = ItemSettlementHistoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolderTransaction(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderTransaction, position: Int) {
        val item = items[position]

        with(holder){
            with(item) {
                var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
                var settleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                var simpleDateFormat2 = SimpleDateFormat("HH:mm")
                var simpleDateFormat3 = SimpleDateFormat("dd/MM/yyyy")
                var sdfTrxHistory = SimpleDateFormat("dd MMM yyyy") //For Trans History //

                date = settleDateFormat.parse(item.transaction_date)
                date2 = simpleDateFormat2.format(date)
                date3 = simpleDateFormat3.format(date)

                binding.startDatetime.text = date2 + " WIB"
                binding.startDatetime.text = date3

                if (item.settlement_date.equals("--")) {
                    if (Locale.getDefault().isO3Language.equals("ind")) {

                        binding.txtSettlementStatus.text = "Tidak Ada Transaksi"
                    } else {
                        binding.txtSettlementStatus.text = "No Transaction"
                    }

                    binding.txtSettlementStatus.setTextColor(context?.resources!!.getColor(R.color.blackGPay))
                } else {
                    if (item.settlement_status.equals("true")) {
                        if (Locale.getDefault().isO3Language.equals("ind")) {

                            binding.txtSettlementStatus.text = "Setelmen Selesai"
                        } else {
                            binding.txtSettlementStatus.text = "Settlement Completed"
                        }

                        binding.txtSettlementStatus.setTextColor(context?.resources!!.getColor(R.color.green_settlement))

                    } else {

                        if (Locale.getDefault().isO3Language.equals("ind")) {
                            binding.txtSettlementStatus.text = "Setelmen Tertunda"
                        } else {
                            binding.txtSettlementStatus.text = "Settlement Pending"
                        }

                        binding.txtSettlementStatus.setTextColor(context?.resources!!.getColor(R.color.orange_settlement))
                    }
                }
                binding.txtTrxQty.text =
                    ConvertData().convertCurency(item.transaction_count!!).get("format") + " trx"
                binding.txtamount.text =
                    "Rp " + ConvertData().convertCurency(item.transaction_amount!!).get("format")
                binding.imageDetail.setOnClickListener {
                    val intent = Intent(context, TransHistoryActivity::class.java)
                    intent.putExtra("tipe_history", "settle")
                    var dateTrxHist = simpleDateFormat3.parse(binding.startDate.text.toString())
                    dateTrxHistory = sdfTrxHistory.format(dateTrxHist)
                    intent.putExtra("settlement_date", dateTrxHistory)
                    context?.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderTransaction(val binding: ItemSettlementHistoryBinding)
        :RecyclerView.ViewHolder(binding.root)

    fun clearAll() {
        var size = itemCount
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun clear() {
        items.clear()
    }

    fun addData(datas: List<ResponseSettlementList.Datum>) {
        items.clear()
        items.addAll(datas)
        notifyDataSetChanged()
    }
}