package com.rygital.moneytracker.utils

import com.rygital.moneytracker.EXPENSE
import com.rygital.moneytracker.INCOME
import com.rygital.moneytracker.INITIAL_ACCOUNTS
import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.utils.calculator.getAccountsData
import com.rygital.moneytracker.utils.calculator.getChartData
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

class FinanceCalculatorTest {

    val usd = Currency(0, "USD", '$', 1.0)
    val eur = Currency(1,"EUR", '\u20AC',0.86)
    val rub = Currency(2,"RUB", '\u20BD', 63.0)

    val transactions = listOf(
            DetailedTransaction(0, EXPENSE, BigDecimal(1), eur.id, eur.label, eur.symbol, eur.rateToUsd,
                    1, 0, 0, 1,"",0, Date()),
            DetailedTransaction(0, INCOME, BigDecimal(2), eur.id, eur.label, eur.symbol, eur.rateToUsd,
                    3, 0, 0, 1,"",0, Date()),
            DetailedTransaction(0, EXPENSE, BigDecimal(3), eur.id, eur.label, eur.symbol, eur.rateToUsd,
                    2, 0, 0, 2,"",0, Date()),
            DetailedTransaction(0, EXPENSE, BigDecimal(1), eur.id, eur.label, eur.symbol, eur.rateToUsd,
                    1, 0, 0, 2,"",0, Date())
    )

    //AccountsData tests

    @Test
    fun `Should return not empty accountsData when no transactions passed`() {
        val accountsData = getAccountsData(Collections.emptyList(), INITIAL_ACCOUNTS.asList(), usd, eur)
        assertEquals(3, accountsData.size)
    }

    @Test
    fun `balances should be same for the same currencies`() {
        val accountsData = getAccountsData(transactions, INITIAL_ACCOUNTS.asList(), usd, usd)
        accountsData.forEach {
            assertEquals(it.primaryBalance, it.secondaryBalance)
        }
    }

    @Test
    fun `Basic accountsData calculation test`() {
        val accountsData = getAccountsData(transactions, INITIAL_ACCOUNTS.asList(), eur, usd)
        assertEquals(1.0, accountsData[0].primaryBalance.toDouble(), 0.01)
        assertEquals(-4.0, accountsData[1].primaryBalance.toDouble(), 0.01)

        assertEquals(accountsData[0].primaryBalance.divide(BigDecimal(eur.rateToUsd), MathContext.DECIMAL128),
                accountsData[0].secondaryBalance)

        assertEquals(accountsData[1].primaryBalance.divide(BigDecimal(eur.rateToUsd), MathContext.DECIMAL128),
                accountsData[1].secondaryBalance)
    }

    // ChartData tests

    @Test
    fun `Should return empty chartData when no transactions passed`() {
        val chartData = getChartData(Collections.emptyList(), usd, EXPENSE)
        assertEquals(0, chartData.size)
    }

    @Test
    fun `Basic chartData calculation test`() {
        val chartData = getChartData(transactions, eur, EXPENSE)
        assertEquals(2, chartData.size)

        assertEquals(3.0, chartData[0].amount.toDouble(), 0.01)
        assertEquals(2.0, chartData[1].amount.toDouble(), 0.01)

    }

}