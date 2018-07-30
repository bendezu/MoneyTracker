package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.math.BigDecimal
import javax.inject.Inject
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.support.v7.widget.DividerItemDecoration


class DashboardFragment: BaseFragment(), Dashboard.View {
    companion object {
        const val TAG: String = "DashboardFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Dashboard.Presenter<Dashboard.View>
    @Inject @JvmSuppressWildcards lateinit var adapter: CategoriesAdapter

    private var onMenuClickListener: OnMenuClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }

        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        App.instance?.componentsHolder
                ?.getDashboardFragmentComponent()?.inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle(R.string.app_name)

        iBtnAddTransaction.setOnClickListener({ onMenuClickListener?.openAddTransactionScreen() })

        val llm = LinearLayoutManager(context)
        rvCategories.layoutManager = llm
        rvCategories.addItemDecoration(DividerItemDecoration(rvCategories.context, llm.orientation))
        rvCategories.setHasFixedSize(true)
        rvCategories.isNestedScrollingEnabled = false
        rvCategories.adapter = adapter

        presenter.loadData()
    }

    override fun showMoneyInRUB(value: BigDecimal) {
        tvRoubles?.text = String.format("₽ %s", formatMoney(value))
    }

    override fun showMoneyInUSD(value: BigDecimal) {
        tvDollars?.text = String.format("$ %s", formatMoney(value))
    }

    override fun showCategories(categoryList: List<Category>, totalExpenses: BigDecimal) {
        adapter.categoryList = categoryList
        drawPieChart(categoryList, totalExpenses)
    }

    private fun drawPieChart(categoryList: List<Category>, totalExpenses: BigDecimal) {

        val entries: MutableList<PieEntry> = mutableListOf()

        for (category in categoryList)
            entries.add(PieEntry(category.plan.toFloat(), category.title))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.setDrawValues(false)

        if (context != null)
            pieDataSet.colors = categoryList.map { it -> ContextCompat.getColor(context!!, it.color) }

        pieChart.data = PieData(pieDataSet)
        pieChart.legend.isEnabled = false
        pieChart.description = null
        pieChart.setDrawEntryLabels(false)
        pieChart.setCenterTextSize(22f)
        pieChart.holeRadius = 80f
        pieChart.isRotationEnabled = false
        pieChart.setEntryLabelTextSize(14f)
        pieChart.centerText = String.format("$ %s", formatMoney(totalExpenses))
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.selSettings -> onMenuClickListener?.openSettingsScreen()
            R.id.selAbout -> onMenuClickListener?.openAboutScreen()
        }

        return false
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseDashboardFragmentComponent()
    }
}