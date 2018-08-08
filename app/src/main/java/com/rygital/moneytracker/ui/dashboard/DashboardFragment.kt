package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import com.rygital.moneytracker.utils.dpToPx
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.math.BigDecimal
import javax.inject.Inject


class DashboardFragment: BaseFragment(), Dashboard.View {
    companion object {
        const val TAG: String = "DashboardFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Dashboard.Presenter<Dashboard.View>
    @Inject lateinit var adapter: CategoriesAdapter

    private lateinit var onMenuClickListener: OnMenuClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }

        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        (App.instance?.componentsHolder?.getComponent(javaClass) as DashboardFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTransaction.setOnClickListener { onMenuClickListener.openAddTransactionScreen(accountPager.currentItem) }
        settings.setOnClickListener { onMenuClickListener.openSettingsScreen() }

        val llm = LinearLayoutManager(context)
        rvCategories.layoutManager = llm
        rvCategories.addItemDecoration(DividerItemDecoration(rvCategories.context, llm.orientation))
        rvCategories.setHasFixedSize(true)
        rvCategories.isNestedScrollingEnabled = false
        rvCategories.adapter = adapter

        accountPager.clipToPadding = false
        accountPager.pageMargin = dpToPx(context!!, 12f).toInt()

        presenter.loadData()
    }

    override fun showPrimaryTotalBalance(value: BigDecimal, symbol: Char) {
        secondaryBalance?.text = "$symbol ${formatMoney(value)}"
    }

    override fun showSecondaryTotalBalance(value: BigDecimal, symbol: Char) {
        primaryBalance?.text = "$symbol ${formatMoney(value)}"
    }

    override fun showAccounts(data: List<AccountPagerItem>) {
        accountPager.adapter = AccountPagerAdapter(data, context, presenter as AccountClickListener)
        accountPager.adapter?.notifyDataSetChanged()
        tabDots.setupWithViewPager(accountPager, true)
    }

    override fun showCategories(categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char) {
        adapter.categoryList = categoryList
        drawPieChart(categoryList, totalExpenses, symbol)
    }

    private fun drawPieChart(categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char) {

        val entries: MutableList<PieEntry> = mutableListOf()

        for (category in categoryList)
            entries.add(PieEntry(category.amount.toFloat(), category.label))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.setDrawValues(false)

        if (context != null)
            pieDataSet.colors = categoryList.map { it -> ContextCompat.getColor(context!!, it.colorRes) }

        pieChart.data = PieData(pieDataSet)
        pieChart.legend.isEnabled = false
        pieChart.description = null
        pieChart.setDrawEntryLabels(false)
        pieChart.setCenterTextSize(22f)
        pieChart.holeRadius = 80f
        pieChart.isRotationEnabled = false
        pieChart.setEntryLabelTextSize(14f)
        pieChart.centerText = "$symbol ${formatMoney(totalExpenses)}"
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun showAccountScreen(accountId: Int) {
        onMenuClickListener.openAccountScreen(accountId)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}