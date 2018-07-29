package com.rygital.moneytracker.ui.dashboard

import android.util.Log
import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(): BasePresenter<V>(), Dashboard.Presenter<V> {

    init {
        Log.d("TAG", "DASHBOARD")
    }

    private val temporaryHardcodedTransactions: List<Transaction> = listOf(
        Transaction(TransactionType.DEBIT, 200.0, Currency.USD),
        Transaction(TransactionType.CREDIT, 5000.0, Currency.RUB),
        Transaction(TransactionType.CREDIT, 100.0, Currency.USD),
        Transaction(TransactionType.DEBIT, 100.0, Currency.USD))

    override fun loadData() {
        val totalSumInUSD: Double = FinanceCalculator.getTotalSum(temporaryHardcodedTransactions)
        val totalSumInRub: Double = totalSumInUSD * FinanceCalculator.TEMPORARY_HARDCODED_RUB_USD_RATE

        view?.showMoneyInUSD(totalSumInUSD)
        view?.showMoneyInRUB(totalSumInRub)
    }
}