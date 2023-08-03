package com.gpay.merchantapp.pages.home.transhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpay.merchantapp.R
import com.gpay.merchantapp.databinding.ItemTransactionHistoryNewBinding
import com.gpay.merchantapp.network.response.ResponseTransactionList
import com.gpay.merchantapp.utils.ConvertData
import java.text.SimpleDateFormat

class TransHistoryAdapter(var context: Context? = null) :
    RecyclerView.Adapter<TransHistoryAdapter.ViewHolderTransaction>() {
    private val items: ArrayList<ResponseTransactionList.Datum> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolderTransaction {
        val binding = ItemTransactionHistoryNewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
                //var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                var simpleDateFormat2 = SimpleDateFormat("HH:mm")
                var simpleDateFormat3 = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

                var date = simpleDateFormat.parse(item.transaction_time)

                val date2 = simpleDateFormat2.format(date)
                val date3 = simpleDateFormat3.format(date)

                binding.startDatetime.text = date2 + " WIB"
                binding.startDate.text = date3
                binding.txtamount.text = "Rp " + ConvertData().convertCurency(item.amount!!).get("format")

                if (item.source_of_fund != null && item.source_of_fund != ""){
                    binding.txtIssuerName.text = item.source_of_fund!!.toLowerCase().capitalize()
                }else{
                    binding.txtIssuerName.text = "Gaja"
                }

                if (binding.txtamount.text.toString().contains("-")) {
                    binding.txtamount.setTextColor(context?.resources!!.getColor(R.color.red))
                } else {
                    binding.txtamount.setTextColor(context?.resources!!.getColor(R.color.green))
                }

                binding.txtIdTransaksi.text = item.reference_number

                if (item.reference_number != null && item.reference_number != "") {
                    binding.txtRefId.text = "#" + item.reference_number
                } else {
                    binding.txtRefId.text = "#"
                }

                binding.txtTransCustomer.text = item.customer_id
            }
        }
    }
    inner class ViewHolderTransaction(val binding: ItemTransactionHistoryNewBinding)
        :RecyclerView.ViewHolder(binding.root)

    fun clear() {
        items.clear()
    }

    fun addData(datas: List<ResponseTransactionList.Datum>) {
        items.clear()
        items.addAll(datas)
        notifyDataSetChanged()
    }
}