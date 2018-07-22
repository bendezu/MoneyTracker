package com.rygital.moneytracker.finance

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import org.junit.Assert.*
import org.junit.Test

class FinanceCalculatorTest {
    private val delta: Double = 0.001

    private val transactions: List<Transaction> = listOf(
            Transaction(TransactionType.DEBIT, 200.0, Currency.USD),
            Transaction(TransactionType.CREDIT, 100.0, Currency.USD),
            Transaction(TransactionType.CREDIT, 100 * FinanceCalculator.TEMPORARY_HARDCODED_RUB_USD_RATE, Currency.RUB),
            Transaction(TransactionType.DEBIT, 300 * FinanceCalculator.TEMPORARY_HARDCODED_RUB_USD_RATE, Currency.RUB),
            Transaction(TransactionType.DEBIT, 100.0, Currency.USD))

    @Test
    fun testTotalSum() {
        assertEquals(400.0, FinanceCalculator.getTotalSum(transactions), delta)
    }

    @Test
    fun testUSDSum() {
        assertEquals(200.0, FinanceCalculator.getUSDSum(transactions), delta)
    }

    @Test
    fun testRubSumInUSD() {
        assertEquals(200.0, FinanceCalculator.getRUBSumInUSD(transactions), delta)
    }

    @Test
    fun testSumming() {
        assertEquals(
                FinanceCalculator.getUSDSum(transactions) + FinanceCalculator.getRUBSumInUSD(transactions),
                FinanceCalculator.getTotalSum(transactions),
                delta)
    }
}