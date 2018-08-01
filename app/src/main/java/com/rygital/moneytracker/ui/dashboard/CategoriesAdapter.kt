package com.rygital.moneytracker.ui.dashboard

import android.support.v4.content.ContextCompat
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

    var categoryList: List<Category> = ArrayList()
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

        fun bind(category: Category) {
            itemView.tvCategoryTitle.text = category.title
            itemView.tvTotal.text = String.format("$ %s", formatMoney(category.fact))
            itemView.cvTag.setCardBackgroundColor(ContextCompat.getColor(itemView.context, category.color))
        }
    }
}