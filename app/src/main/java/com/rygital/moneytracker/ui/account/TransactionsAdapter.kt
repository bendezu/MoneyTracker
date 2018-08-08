package com.rygital.moneytracker.ui.account

import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.EXPENSE
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.utils.formatMoney
import com.rygital.moneytracker.utils.formatToYesterdayOrToday
import kotlinx.android.synthetic.main.transaction_item.view.*
import javax.inject.Inject

@FragmentScope
class TransactionsAdapter @Inject constructor() : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    var transactionList: List<DetailedTransaction> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = transactionList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var id: Long = 0

        fun bind(transaction: DetailedTransaction) {
            id = transaction.id
            itemView.cvTag.setCardBackgroundColor(getColor(itemView.context, transaction.categoryColor))
            itemView.label.text = itemView.context.getText(transaction.categoryLabel)
            val today = itemView.context.getString(R.string.today)
            val yesterday = itemView.context.getString(R.string.yesterday)
            itemView.date.text = formatToYesterdayOrToday(transaction.date, today, yesterday)
            val sign = if (transaction.type == EXPENSE) "-" else "+"
            itemView.amount.text = "$sign ${formatMoney(transaction.amount)} ${transaction.currencySymbol}"
        }
    }
}