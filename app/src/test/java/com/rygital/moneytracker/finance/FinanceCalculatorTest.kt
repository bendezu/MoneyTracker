package com.rygital.moneytracker.finance

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.data.model.Transaction
import com.rygital.moneytracker.data.model.TransactionType
import com.rygital.moneytracker.utils.calculator.FinanceCalculator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class FinanceCalculatorTest {
    private var financeCalculator: FinanceCalculator? = null
    private val rubUsdRate: BigDecimal = BigDecimal("60")

    private val transactions: List<Transaction> = listOf(
            Transaction(TransactionType.DEBIT, BigDecimal("200"), Currency.USD),
            Transaction(TransactionType.CREDIT, BigDecimal("100"), Currency.USD),
            Transaction(TransactionType.CREDIT, rubUsdRate.multiply(BigDecimal("100")), Currency.RUB),
            Transaction(TransactionType.DEBIT, rubUsdRate.multiply(BigDecimal("300")), Currency.RUB),
            Transaction(TransactionType.DEBIT, BigDecimal("100"), Currency.USD))

    @Before
    fun beforeTests() {
        financeCalculator = FinanceCalculator(rubUsdRate)
    }

    @Test
    fun testTotalSum() {
        assertEquals(BigDecimal("400"), financeCalculator?.getTotalSum(transactions))
    }

    @Test
    fun testUSDSum() {
        assertEquals(BigDecimal("200"), financeCalculator?.getUSDSum(transactions))
    }

    @Test
    fun testRubSumInUSD() {
        assertEquals(BigDecimal("200"), financeCalculator?.getRUBSumInUSD(transactions))
    }

    @Test
    fun testSumming() {
        assertEquals(
                financeCalculator?.getUSDSum(transactions)?.add(financeCalculator?.getRUBSumInUSD(transactions)),
                financeCalculator?.getTotalSum(transactions))
    }
}