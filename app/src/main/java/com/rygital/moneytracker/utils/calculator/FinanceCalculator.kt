package com.rygital.moneytracker.utils.calculator

import com.rygital.moneytracker.EXPENSE
import com.rygital.moneytracker.INCOME
import com.rygital.moneytracker.data.model.database.*
import com.rygital.moneytracker.ui.dashboard.AccountPagerItem
import com.rygital.moneytracker.ui.dashboard.ChartItem
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun getAccountsData(transactions: List<DetailedTransaction>,
                    primaryCurrency: Currency, secondaryCurrency: Currency): List<AccountPagerItem> {
    return transactions.groupBy { it.accountId }.entries.sortedBy { it.key }.map { it.value }.map {
        buildAccountData(it, primaryCurrency, secondaryCurrency)
    }
}

private fun buildAccountData(transactions: List<DetailedTransaction>,
                     primaryCurrency: Currency, secondaryCurrency: Currency): AccountPagerItem{
    val icon: Int = transactions[0].accountIcon
    val label: Int = transactions[0].accountLabel
    val usdBalance = calculateSum(transactions)
    val primaryBalance = usdBalance.multiply(BigDecimal(primaryCurrency.rateToUsd))
    val secondaryBalance = usdBalance.multiply(BigDecimal(secondaryCurrency.rateToUsd))

    return AccountPagerItem(icon, label, primaryBalance, primaryCurrency.symbol, secondaryBalance, secondaryCurrency.symbol)
}

private fun calculateSum(transactions: List<DetailedTransaction>): BigDecimal {
    var sum = BigDecimal.ZERO
    transactions.forEach {
        if (it.type == INCOME)
            sum += it.amount.divide(BigDecimal(it.currencyRate), MathContext.DECIMAL128)
        if (it.type == EXPENSE)
            sum -= it.amount.divide(BigDecimal(it.currencyRate), MathContext.DECIMAL128)
    }
    return sum
}

fun getChartData(transactions: List<DetailedTransaction>, primaryCurrency: Currency): List<ChartItem> {
     return transactions.filter { it.type == EXPENSE }.groupBy { it.categoryId }.map {
        buildChartData(it.value, primaryCurrency)
    }.sortedByDescending { it.amount }
}

private fun buildChartData(transactions: List<DetailedTransaction>, primaryCurrency: Currency): ChartItem {
    val label = transactions[0].categoryLabel
    val colorRes = transactions[0].categoryColor
    var usdSum = BigDecimal.ZERO
    transactions.forEach {
        usdSum += it.amount.divide(BigDecimal(it.currencyRate), MathContext.DECIMAL128)
    }
    val sum = usdSum.multiply(BigDecimal(primaryCurrency.rateToUsd))
    return ChartItem(colorRes, label, sum)
}
