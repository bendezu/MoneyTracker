package com.rygital.moneytracker.utils.calculator

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType

class FinanceCalculator {
    companion object {
        const val TEMPORARY_HARDCODED_RUB_USD_RATE: Double = 63.46431

        fun getTotalSum(transactions: List<Transaction>): Double {
            return getUSDSum(transactions) + getRUBSumInUSD(transactions)
        }

        fun getUSDSum(transactions: List<Transaction>): Double {
            val usdTransactions: List<Transaction> = transactions.filter { it.currency == Currency.USD }
            return getSum(usdTransactions)
        }

        fun getRUBSumInUSD(transactions: List<Transaction>): Double {
            val rubTransactions: List<Transaction> = transactions.filter { it.currency == Currency.RUB }
            return getSum(rubTransactions) / TEMPORARY_HARDCODED_RUB_USD_RATE
        }

        private fun getSum(transactions: List<Transaction>) : Double {
            var sum = 0.0
            for (transaction in transactions) {
                if (transaction.transactionType == TransactionType.DEBIT)
                    sum += transaction.value
                else
                    sum -= transaction.value
            }

            return sum
        }
    }
}