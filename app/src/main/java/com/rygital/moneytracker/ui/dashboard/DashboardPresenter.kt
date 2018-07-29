package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import java.math.BigDecimal
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(): BasePresenter<V>(), Dashboard.Presenter<V> {

    private val temporaryHardcodedRubUsdRate: BigDecimal = BigDecimal("60")

    private val temporaryHardcodedTransactions: List<Transaction> = listOf(
        Transaction(TransactionType.DEBIT, BigDecimal("200.0"), Currency.USD),
        Transaction(TransactionType.CREDIT, BigDecimal("5000.0"), Currency.RUB),
        Transaction(TransactionType.CREDIT, BigDecimal("100.0"), Currency.USD),
        Transaction(TransactionType.DEBIT, BigDecimal("100.0"), Currency.USD))

    override fun loadData() {
        val financeCalculator = FinanceCalculator(temporaryHardcodedRubUsdRate)

        val totalSumInUSD: BigDecimal = financeCalculator.getTotalSum(temporaryHardcodedTransactions)
        val totalSumInRub: BigDecimal = totalSumInUSD.multiply(temporaryHardcodedRubUsdRate)

        view?.showMoneyInUSD(totalSumInUSD)
        view?.showMoneyInRUB(totalSumInRub)
    }
}