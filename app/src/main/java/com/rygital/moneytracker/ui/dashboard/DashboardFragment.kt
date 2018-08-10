package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.rygital.moneytracker.*
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.ui.account.SwipeToDeleteCallback
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import com.rygital.moneytracker.utils.formatMoney
import com.rygital.moneytracker.utils.getWelcomeMessage
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.math.BigDecimal
import javax.inject.Inject


class DashboardFragment: BaseFragment(), Dashboard.View {
    companion object {
        const val TAG: String = "DashboardFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Dashboard.Presenter<Dashboard.View>
    @Inject lateinit var expensesAdapter: CategoriesAdapter
    @Inject lateinit var incomesAdapter: CategoriesAdapter
    @Inject lateinit var patternsAdapter: PatternsAdapter
    private lateinit var accountsAdapter: AccountPagerAdapter
    private var currentPage: Int = 0

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

        linear.requestFocus()
        welcome.setText(getWelcomeMessage())

        addTransaction.setOnClickListener {
            val accountPosition = if (accountPager.currentItem != accountsAdapter.data.size) accountPager.currentItem else 0
            onMenuClickListener.openAddTransactionScreen(accountPosition)
        }
        settings.setOnClickListener { onMenuClickListener.openSettingsScreen() }

        val expensesllm = LinearLayoutManager(context)
        rvExpenseCategories.layoutManager = expensesllm
        rvExpenseCategories.addItemDecoration(DividerItemDecoration(rvExpenseCategories.context, expensesllm.orientation))
        rvExpenseCategories.setHasFixedSize(true)
        rvExpenseCategories.isNestedScrollingEnabled = false
        rvExpenseCategories.adapter = expensesAdapter

        val incomesllm = LinearLayoutManager(context)
        rvIncomeCategories.layoutManager = incomesllm
        rvIncomeCategories.addItemDecoration(DividerItemDecoration(rvIncomeCategories.context, incomesllm.orientation))
        rvIncomeCategories.setHasFixedSize(true)
        rvIncomeCategories.isNestedScrollingEnabled = false
        rvIncomeCategories.adapter = incomesAdapter

        rvPatterns.layoutManager = LinearLayoutManager(context)
        rvPatterns.setHasFixedSize(true)
        rvPatterns.adapter = patternsAdapter
        checkPatternsCount()
        ItemTouchHelper(object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val patternId = (viewHolder as PatternsAdapter.ViewHolder).id
                patternsAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                presenter.deletePattern(patternId)
                checkPatternsCount()
            }
        }).attachToRecyclerView(rvPatterns)

        accountsAdapter = AccountPagerAdapter(context, presenter)
        accountPager.adapter = accountsAdapter
        accountPager.clipToPadding = false
        tabDots.setupWithViewPager(accountPager, true)
        accountPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })

        expensesPeriodSpinner.adapter = getAdapter(REPORT_INTERVALS.map { context?.getString(it) })
        expensesPeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        ?.edit()?.putInt(PREF_KEY_EXPENSES_PERIOD, position)?.apply()
                presenter.updateExpensesChart(position)
            }
        }
        val initialExpensePeriod = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.getInt(PREF_KEY_EXPENSES_PERIOD, 2) ?: 2
        expensesPeriodSpinner.setSelection(initialExpensePeriod)

        incomePeriodSpinner.adapter = getAdapter(REPORT_INTERVALS.map { context?.getString(it) })
        incomePeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        ?.edit()?.putInt(PREF_KEY_INCOMES_PERIOD, position)?.apply()
                presenter.updateIncomesChart(position)
            }
        }
        val initialIncomePeriod = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.getInt(PREF_KEY_INCOMES_PERIOD, 2) ?: 2
        incomePeriodSpinner.setSelection(initialIncomePeriod)

        presenter.loadData()
        presenter.updateExpensesChart(initialExpensePeriod)
        presenter.updateIncomesChart(initialIncomePeriod)
    }

    private fun<T> getAdapter(list: List<T>): ArrayAdapter<T> {
        val dataAdapterCategory = ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list)
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapterCategory
    }

    override fun showPrimaryTotalBalance(value: BigDecimal, symbol: Char) {
        secondaryBalance?.text = "$symbol ${formatMoney(value)}"
    }

    override fun showSecondaryTotalBalance(value: BigDecimal, symbol: Char) {
        primaryBalance?.text = "$symbol ${formatMoney(value)}"
    }

    override fun showAccounts(data: List<AccountPagerItem>) {
        accountsAdapter.data = data
        accountPager.currentItem = currentPage
    }

    override fun showPatterns(data: List<DetailedTransaction>) {
        patternsAdapter.patternList = data
        checkPatternsCount()
    }

    private fun checkPatternsCount() {
        if (patternsAdapter.itemCount == 0) {
            rvPatterns.visibility = View.GONE
            emptyPatterns.visibility = View.VISIBLE
        } else {
            emptyPatterns.visibility = View.GONE
            rvPatterns.visibility = View.VISIBLE
        }
    }

    override fun showExpenseCategories(categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char) {
        expensesAdapter.categoryList = categoryList
        drawPieChart(expensesChart, categoryList, totalExpenses, symbol)
    }

    override fun showIncomeCategories(categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char) {
        incomesAdapter.categoryList = categoryList
        drawPieChart(incomesChart, categoryList, totalExpenses, symbol)
    }

    private fun drawPieChart(chart: PieChart, categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char) {

        val entries: MutableList<PieEntry> = mutableListOf()

        for (category in categoryList)
            entries.add(PieEntry(category.amount.toFloat(), category.label))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.setDrawValues(false)

        if (context != null)
            pieDataSet.colors = categoryList.map { it -> ContextCompat.getColor(context!!, it.colorRes) }

        if (categoryList.isEmpty()) {
            entries.add(PieEntry(1f))
            pieDataSet.colors = listOf(Color.LTGRAY)
        }

        chart.data = PieData(pieDataSet)
        chart.legend.isEnabled = false
        chart.description = null
        chart.animateY(1000, Easing.EasingOption.EaseInOutQuad)
        chart.setDrawEntryLabels(false)
        chart.isHighlightPerTapEnabled = false
        chart.setCenterTextSize(22f)
        chart.holeRadius = 85f
        chart.isRotationEnabled = false
        chart.setEntryLabelTextSize(14f)
        chart.centerText = "$symbol ${formatMoney(totalExpenses)}"
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun showAccountScreen(accountId: Int) {
        onMenuClickListener.openAccountScreen(accountId)
    }

    override fun showAddAccountScreen() {
        onMenuClickListener.openAddAccountScreen()
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}