package com.rygital.moneytracker.utils.calculator

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType
import java.math.BigDecimal

class FinanceCalculator(private var rubUsdRate: BigDecimal) {
    fun getTotalSum(transactions: List<Transaction>): BigDecimal {
        return getUSDSum(transactions) + getRUBSumInUSD(transactions)
    }

    fun getUSDSum(transactions: List<Transaction>): BigDecimal {
        val usdTransactions: List<Transaction> = transactions.filter { it.currency == Currency.USD }
        return getSum(usdTransactions)
    }

    fun getRUBSumInUSD(transactions: List<Transaction>): BigDecimal {
        val rubTransactions: List<Transaction> = transactions.filter { it.currency == Currency.RUB }
        return getSum(rubTransactions) / rubUsdRate
    }

    private fun getSum(transactions: List<Transaction>): BigDecimal {
        var sum: BigDecimal = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.transactionType == TransactionType.DEBIT)
                sum += transaction.value
            else
                sum -= transaction.value
        }

        return sum
    }
}