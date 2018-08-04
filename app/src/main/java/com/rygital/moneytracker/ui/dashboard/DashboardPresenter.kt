package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import com.rygital.moneytracker.PREF_KEY_PRIMARY_CURRENCY
import com.rygital.moneytracker.PREF_KEY_SECONDARY_CURRENCY
import com.rygital.moneytracker.PREF_NAME
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.local.DatabaseHelper
import com.rygital.moneytracker.data.model.Account
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.data.model.UsdBasedRates
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.*
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class DashboardPresenter<V : Dashboard.View> @Inject constructor(private val databaseHelper: DatabaseHelper,
                                                                 private val currencyRatesManager: CurrencyRatesManager,
                                                                 private val schedulerProvider: SchedulerProvider,
                                                                 private val context: Context)
    : BasePresenter<V>(), Dashboard.Presenter<V> {

    override fun loadData() {
        addDisposable(currencyRatesManager.getRates()
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableObserver<UsdBasedRates>() {
                    override fun onComplete() {
                        loadFromDatabase()
                    }

                    override fun onNext(rates: UsdBasedRates) {
                        val currencyDao = FinanceDatabase.getInstance(context)?.currencyDao()
                        if (currencyDao != null) {
                            val currencies = currencyDao.getAll()
                            currencies.forEach { it.rateToUsd = rates.getRateByCurrency(it.label).toDouble() }
                            currencyDao.updateCurrencies(currencies)
                        } else {
                            Timber.e("Can't load database")
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.showMessage(R.string.error_loadings_rates)
                        Timber.e(e)
                        e.printStackTrace()
                        loadFromDatabase()
                    }

                    fun loadFromDatabase() {
                        val database = FinanceDatabase.getInstance(context)
                        if (database != null) {
                            addDisposable(database.transactionDao().getDetailedTransactions()
                                    .map {
                                        val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                                        val primaryCurrencyId = preferences.getInt(PREF_KEY_PRIMARY_CURRENCY, 0)
                                        val secondaryCurrencyId = preferences.getInt(PREF_KEY_SECONDARY_CURRENCY, 0)
                                        val primaryCurrency = database.currencyDao().findById(primaryCurrencyId.toLong())
                                        val secondaryCurrency = database.currencyDao().findById(secondaryCurrencyId.toLong())

                                        val accountsData = getAccountsData(it, primaryCurrency, secondaryCurrency)

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
                        } else {
                            Timber.e("Can't load database")
                        }
                    }

                })
        )

//        addDisposable(Observable
//                .zip(currencyRatesManager.getRates(),
//                        databaseHelper.getTransactions(),
//                        databaseHelper.getCategories(),
//                        databaseHelper.getAccounts(),
//                        Function4 { usdBasedRates: UsdBasedRates,
//                                    transactions: List<Transaction>,
//                                    categories: List<Category>,
//                                    accounts: List<Account> ->
//                            val financeCalculator = FinanceCalculator(usdBasedRates)
//                            for (category in categories)
//                                category.fact = financeCalculator.getExpensesByCategory(transactions, category)
//
//                            val totalExpenses = financeCalculator.getTotalExpenses(transactions)
//
//                            val totalSumInUSD: BigDecimal = financeCalculator.getTotalSum(transactions)
//                            val totalSumInRub: BigDecimal = totalSumInUSD.multiply(usdBasedRates.rub)
//
//                            val cashSum: BigDecimal = financeCalculator.getSumOnAccount(transactions, accounts[0])
//                            val bankCardSum: BigDecimal = financeCalculator.getSumOnAccount(transactions, accounts[1])
//
//                            DashboardViewState(totalSumInUSD, totalSumInRub,
//                                    cashSum, bankCardSum,
//                                    categories, totalExpenses)
//                        })
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribeWith(object: DisposableObserver<DashboardViewState>() {
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(dashboardViewState: DashboardViewState) {
//                        view?.showSecondaryTotalBalance(dashboardViewState.totalPrimarySum)
//                        view?.showPrimaryTotalBalance(dashboardViewState.totalSecondarySum)
//
//                        view?.showAccounts(dashboardViewState.accountPagerData)
//
//                        view?.showCategories(dashboardViewState.chartData, dashboardViewState.totalExpenses)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        view?.showMessage(R.string.error_loadings_rates)
//                        Timber.e(e)
//                        e.printStackTrace()
//                    }
//                }))
    }
}