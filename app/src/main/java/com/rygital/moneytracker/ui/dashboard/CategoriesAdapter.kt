package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.utils.formatMoney
import javax.inject.Inject

@FragmentScope
class CategoriesAdapter @Inject constructor(@JvmSuppressWildcards var presenter: Dashboard.Presenter<Dashboard.View>)
    : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

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

//        holder.llItem.setTag(position)
//        holder.llItem.setOnClickListener({ v -> presenter.openPicture(v.getTag() as Int) })
    }

    class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var tvCategoryTitle: TextView? = null
        private var tvTotal: TextView? = null
        private var cvTag: CardView? = null

        init {
            tvCategoryTitle = itemView.findViewById(R.id.tvCategoryTitle)
            tvTotal = itemView.findViewById(R.id.tvTotal)
            cvTag = itemView.findViewById(R.id.cvTag)
        }

        fun bind(category: Category) {
            tvCategoryTitle?.text = category.title
            tvTotal?.text = String.format("$ %s", formatMoney(category.fact))

            cvTag?.setCardBackgroundColor(ContextCompat.getColor(itemView.context, category.color))
        }
    }
}