package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import com.rygital.moneytracker.PREF_KEY_PRIMARY_CURRENCY
import com.rygital.moneytracker.PREF_KEY_SECONDARY_CURRENCY
import com.rygital.moneytracker.PREF_NAME
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.UsdBasedRates
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.data.model.database.Transaction
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.CurrencyRatesManager
import com.rygital.moneytracker.utils.calculator.getAccountsData
import com.rygital.moneytracker.utils.calculator.getChartData
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread

class DashboardPresenter<V : Dashboard.View> @Inject constructor(private val currencyRatesManager: CurrencyRatesManager,
                                                                 private val schedulerProvider: SchedulerProvider,
                                                                 private val context: Context,
                                                                 private val database: FinanceDatabase)
    : BasePresenter<V>(), Dashboard.Presenter<V> {

    override fun loadData() {
        updateCurrencyRates()
        calculateData()
        loadPatterns()
    }

    private fun updateCurrencyRates() {
        addDisposable(currencyRatesManager.getRates()
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableObserver<UsdBasedRates>() {
                    override fun onComplete() {}

                    override fun onNext(rates: UsdBasedRates) {
                        val currencyDao = database.currencyDao()
                        val currencies = currencyDao.getAll()
                        currencies.forEach { it.rateToUsd = rates.getRateByCurrency(it.label).toDouble() }
                        currencyDao.updateCurrencies(currencies)
                    }

                    override fun onError(e: Throwable) {
                        view?.showMessage(R.string.error_loadings_rates)
                        Timber.e(e)
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun calculateData() {
        addDisposable(
                database.transactionDao().getDetailedTransactions()
                .map {

                    val accounts = database.accountDao().getAll()

                    val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val primaryCurrencyId = preferences.getInt(PREF_KEY_PRIMARY_CURRENCY, 0)
                    val secondaryCurrencyId = preferences.getInt(PREF_KEY_SECONDARY_CURRENCY, 0)
                    val primaryCurrency = database.currencyDao().findById(primaryCurrencyId.toLong())
                    val secondaryCurrency = database.currencyDao().findById(secondaryCurrencyId.toLong())

                    val accountsData = getAccountsData(it, accounts, primaryCurrency, secondaryCurrency)

                    var totalPrimarySum = BigDecimal.ZERO
                    accountsData.forEach { totalPrimarySum += it.primaryBalance }

                    var totalSecondarySum = BigDecimal.ZERO
                    accountsData.forEach { totalSecondarySum += it.secondaryBalance }

                    val chartData = getChartData(it, primaryCurrency)

                    var totalExpenses = BigDecimal.ZERO
                    chartData.forEach { totalExpenses += it.amount }

                    DashboardViewState(
                            totalPrimarySum,
                            totalSecondarySum,
                            accountsData,
                            totalExpenses,
                            chartData,
                            primaryCurrency,
                            secondaryCurrency)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ viewState ->

                    view?.showPrimaryTotalBalance(viewState.totalSecondarySum,
                            viewState.secondaryCurrency.symbol)

                    view?.showSecondaryTotalBalance(viewState.totalPrimarySum,
                            viewState.primaryCurrency.symbol)

                    view?.showAccounts(viewState.accountPagerData)

                    view?.showCategories(viewState.chartData, viewState.totalExpenses,
                            viewState.primaryCurrency.symbol)

                }, { err ->
                    view?.showMessage(R.string.error_loadings_rates)
                    Timber.e(err)
                    err.printStackTrace()
                }))
    }

    private fun loadPatterns() {
        addDisposable(database.patternDao().getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe{
                    view?.showPatterns(it)
                }
        )
    }

    override fun deletePattern(id: Long) {
        thread {
            database.patternDao().delete(id)
        }
    }

    override fun addTransaction(pattern: DetailedTransaction) {
        thread {
            val transaction = Transaction(pattern.type, pattern.amount, pattern.currencyId,
                    pattern.categoryId, pattern.accountId, Date())
            database.transactionDao().insert(transaction)
        }
    }

    override fun openAccountScreen(position: Int) {
        view?.showAccountScreen(position)
    }

    override fun openAddAccountScreen() {
        view?.showAddAccountScreen()
    }
}