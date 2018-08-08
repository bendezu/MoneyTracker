package com.rygital.moneytracker.ui.dashboard

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
import kotlinx.android.synthetic.main.pattern_item.view.*
import javax.inject.Inject

@FragmentScope
class PatternsAdapter @Inject constructor(private val preserter: Dashboard.Presenter<Dashboard.View>)
    : RecyclerView.Adapter<PatternsAdapter.ViewHolder>() {

    var patternList: List<DetailedTransaction> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.pattern_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = patternList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(patternList[position])
        holder.itemView.setOnClickListener { preserter.addTransaction(patternList[position]) }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var id: Long = 0

        fun bind(pattern: DetailedTransaction) {
            id = pattern.id
            itemView.cvTag.setCardBackgroundColor(getColor(itemView.context, pattern.categoryColor))
            itemView.label.text = itemView.context.getText(pattern.categoryLabel)
            itemView.account.text = itemView.context.getText(pattern.accountLabel)
            itemView.icon.setImageResource(pattern.accountIcon)
            val sign = if (pattern.type == EXPENSE) "-" else "+"
            itemView.amount.text = "$sign ${formatMoney(pattern.amount)} ${pattern.currencySymbol}"
        }
    }
}