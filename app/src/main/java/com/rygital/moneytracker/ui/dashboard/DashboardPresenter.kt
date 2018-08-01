package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.local.DatabaseHelper
import com.rygital.moneytracker.data.model.Account
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.data.model.UsdBasedRates
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.CurrencyRatesManager
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(private val databaseHelper: DatabaseHelper,
                                                                private val currencyRatesManager: CurrencyRatesManager,
                                                                private val schedulerProvider: SchedulerProvider)
    : BasePresenter<V>(), Dashboard.Presenter<V> {

    override fun loadData() {
        addDisposable(Observable
                .zip(currencyRatesManager.getRates(), databaseHelper.getTransactions(), databaseHelper.getCategories(),
                        databaseHelper.getAccounts(),
                        Function4 { usdBasedRates: UsdBasedRates,
                                    transactions: List<Transaction>,
                                    categories: List<Category>,
                                    accounts: List<Account> ->
                            val financeCalculator = FinanceCalculator(usdBasedRates)
                            for (category in categories)
                                category.fact = financeCalculator.getExpensesByCategory(transactions, category)

                            val totalExpenses = financeCalculator.getTotalExpenses(transactions)

                            val totalSumInUSD: BigDecimal = financeCalculator.getTotalSum(transactions)
                            val totalSumInRub: BigDecimal = totalSumInUSD.multiply(usdBasedRates.rub)

                            val cashSum: BigDecimal = financeCalculator.getSumOnAccount(transactions, accounts[0])
                            val bankCardSum: BigDecimal = financeCalculator.getSumOnAccount(transactions, accounts[1])

                            DashboardViewState(totalSumInUSD, totalSumInRub,
                                    cashSum, bankCardSum,
                                    categories, totalExpenses)
                        })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object: DisposableObserver<DashboardViewState>() {
                    override fun onComplete() {
                    }

                    override fun onNext(dashboardViewState: DashboardViewState) {
                        view?.showMoneyInUSD(dashboardViewState.totalSumInUSD)
                        view?.showMoneyInRUB(dashboardViewState.totalSumInRub)

                        view?.showCashSum(dashboardViewState.cashSum)
                        view?.showBackCardSum(dashboardViewState.bankCardSum)

                        view?.showCategories(dashboardViewState.categories, dashboardViewState.totalExpenses)
                    }

                    override fun onError(e: Throwable) {
                        view?.showMessage(R.string.error_loadings_rates)
                        Timber.e(e)
                        e.printStackTrace()
                    }
                }))
    }
}