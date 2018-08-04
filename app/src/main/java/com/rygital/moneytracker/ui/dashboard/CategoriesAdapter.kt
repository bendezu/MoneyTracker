package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.item_category.view.*
import javax.inject.Inject

@FragmentScope
class CategoriesAdapter @Inject constructor() : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    var categoryList: List<ChartItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoriesViewHolder(v)
    }

    override fun getItemCount(): Int = categoryList.count()

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(category: ChartItem) {
            itemView.tvCategoryTitle.setText(category.label)
            itemView.tvTotal.text = formatMoney(category.amount)
            itemView.cvTag.setCardBackgroundColor(getColor(itemView.context, category.colorRes))
        }
    }
}