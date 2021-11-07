package com.exchangerates.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exchangerates.R
import com.exchangerates.model.Exchange

class ExchangesAdapter(
    private var todayExchangesList: List<Exchange>,
    private var tomorrowExchangesList: List<Exchange>
): RecyclerView.Adapter<ExchangesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var exchangeName: TextView = view.findViewById(R.id.exchange_name_textView)
        var exchangeDescription: TextView = view.findViewById(R.id.exchange_description_textView)
        var exchangeFirstRates: TextView = view.findViewById(R.id.exchange_first_rates_textView)
        var exchangeSecondRates: TextView = view.findViewById(R.id.exchange_second_rates_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exchange = todayExchangesList[position]
        holder.exchangeName.text = todayExchangesList[position].Cur_Abbreviation
        holder.exchangeDescription.text = todayExchangesList[position].Cur_Name
        holder.exchangeFirstRates.text = todayExchangesList[position].Cur_OfficialRate
        //holder.exchangeName.text = tomorrowExchangesList[position].Cur_OfficialRate
    }

    override fun getItemCount() = todayExchangesList.size

    fun setExchangesLists(newTodayExchangesList: List<Exchange>, newTomorrowExchangesList: List<Exchange>) {
        todayExchangesList = newTodayExchangesList
        tomorrowExchangesList = newTomorrowExchangesList
        notifyDataSetChanged()
    }

}