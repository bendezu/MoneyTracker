package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.local.DatabaseHelper
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.data.model.UsdBasedRates
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.CurrencyRatesManager
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(private val databaseHelper: DatabaseHelper,
                                                                private val currencyRatesManager: CurrencyRatesManager,
                                                                private val schedulerProvider: SchedulerProvider)
    : BasePresenter<V>(), Dashboard.Presenter<V> {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadData() {
        compositeDisposable.add(Observable
                .zip(currencyRatesManager.getRates(), databaseHelper.getTransactions(), databaseHelper.getCategories(),
                        Function3 { usdBasedRates: UsdBasedRates,
                                    transactions: List<Transaction>,
                                    categories: List<Category> ->
                            val financeCalculator = FinanceCalculator(usdBasedRates)
                            for (category in categories)
                                category.fact = financeCalculator.getExpensesByCategory(transactions, category)

                            val totalExpenses = financeCalculator.getTotalExpenses(transactions)

                            val totalSumInUSD: BigDecimal = financeCalculator.getTotalSum(transactions)
                            val totalSumInRub: BigDecimal = totalSumInUSD.multiply(usdBasedRates.rub)

                            DashboardViewState(totalSumInUSD, totalSumInRub, categories, totalExpenses)
                        })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object: DisposableObserver<DashboardViewState>() {
                    override fun onComplete() {
                    }

                    override fun onNext(dashboardViewState: DashboardViewState) {
                        view?.showMoneyInUSD(dashboardViewState.totalSumInUSD)
                        view?.showMoneyInRUB(dashboardViewState.totalSumInRub)

                        view?.showCategories(dashboardViewState.categories, dashboardViewState.totalExpenses)
                    }

                    override fun onError(e: Throwable) {
                        view?.showMessage(R.string.error_loadings_rates)
                        Timber.e(e)
                        e.printStackTrace()
                    }
                }))
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }
}