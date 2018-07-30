package com.rygital.moneytracker.utils.calculator

import com.rygital.moneytracker.data.model.*
import java.math.BigDecimal
import javax.inject.Inject

class FinanceCalculator @Inject constructor(private var usdBasedRates: UsdBasedRates) {

    fun getSumOnAccount(transactions: List<Transaction>, account: Account): BigDecimal {
        return getTotalSum(transactions.filter { it.accountId == account.id })
    }

    fun getTotalExpenses(transactions: List<Transaction>): BigDecimal {
        return getTotalSum(transactions.filter { it.transactionType == TransactionType.CREDIT }).abs()
    }

    fun getExpensesByCategory(transactions: List<Transaction>, category: Category): BigDecimal {
        return getTotalSum(transactions
                .filter { it.transactionType == TransactionType.CREDIT && it.categoryId == category.id })
                .abs()
    }

    fun getTotalSum(transactions: List<Transaction>): BigDecimal {
        var sum = BigDecimal.ZERO
        for (currency in Currency.values()) {
            sum += getSum(transactions, currency)
        }

        return sum
    }

    fun getSum(transactions: List<Transaction>, currency: Currency): BigDecimal {
        val filteredTransactions: List<Transaction> = transactions.filter { it.currency == currency }
        return getSum(filteredTransactions) / usdBasedRates.getRateByCurrency(currency)
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