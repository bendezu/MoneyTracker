package com.rygital.moneytracker.finance

import com.rygital.moneytracker.data.model.*
import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class FinanceCalculatorTest {
    private var financeCalculator: FinanceCalculator? = null
    private val rates = UsdBasedRates(BigDecimal("60"), BigDecimal("1.15"))

    private val transactions: List<Transaction> = listOf(
            Transaction(TransactionType.DEBIT, BigDecimal("200"), Currency.USD, 1, 0, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("100"), Currency.USD, 1, 1, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, rates.rub.multiply(BigDecimal("100")), Currency.RUB, 1, 2, Calendar.getInstance().time),
            Transaction(TransactionType.DEBIT, rates.rub.multiply(BigDecimal("300")), Currency.RUB, 0, 0, Calendar.getInstance().time),
            Transaction(TransactionType.DEBIT, BigDecimal("100"), Currency.USD, 0, 0, Calendar.getInstance().time))

    @Before
    fun beforeTests() {
        financeCalculator = FinanceCalculator(rates)
    }

    @Test
    fun testSumOnAccount() {
        assertEquals(BigDecimal("0.00"), financeCalculator?.getSumOnAccount(transactions, Account(1, "", "")))
    }

    @Test
    fun testTotalExpenses() {
        assertEquals(BigDecimal("200.00"), financeCalculator?.getTotalExpenses(transactions))
    }

    @Test
    fun testExpensesByCategory() {
        assertEquals(BigDecimal("100.00"), financeCalculator?.getExpensesByCategory(transactions, Category(2, "", BigDecimal.ZERO)))
    }

    @Test
    fun testTotalSum() {
        assertEquals(BigDecimal("400.00"), financeCalculator?.getTotalSum(transactions))
    }

    @Test
    fun testUSDSum() {
        assertEquals(BigDecimal("200.00"), financeCalculator?.getSum(transactions, Currency.USD))
    }

    @Test
    fun testRubSumInUSD() {
        assertEquals(BigDecimal("200.00"), financeCalculator?.getSum(transactions, Currency.RUB))
    }

    @Test
    fun testSumming() {
        assertEquals(
                financeCalculator?.getSum(transactions, Currency.USD)
                        ?.add(financeCalculator?.getSum(transactions, Currency.RUB)),
                financeCalculator?.getTotalSum(transactions))
    }
}