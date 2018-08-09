package com.rygital.moneytracker

import com.rygital.moneytracker.data.model.database.Account
import com.rygital.moneytracker.data.model.database.Category
import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.data.model.database.Transaction
import java.math.BigDecimal
import java.util.*

const val ENDPOINT = "https://openexchangerates.org/api/"
const val API_KEY = "ce9f17c314c840ef8b5d8fd6d472bc4c"

const val PREF_NAME = "pref_currencies"
const val PREF_KEY_PRIMARY_CURRENCY = "primary_currency"
const val PREF_KEY_SECONDARY_CURRENCY = "secondary_currency"

const val EXPENSE = 0
const val INCOME = 1

val INTERVALS = arrayOf(
        R.string.day,
        R.string.week,
        R.string.month,
        R.string.year
)

val REPORT_INTERVALS = arrayOf(
        R.string.day,
        R.string.week,
        R.string.month,
        R.string.year,
        R.string.all_time
)

val INITIAL_CURRENCIES = arrayOf(
        Currency(0, "USD", '$', 1.0),
        Currency(1,"EUR", '\u20AC',0.86),
        Currency(2,"RUB", '\u20BD', 63.0)
)

val INITIAL_CATEGORIES = arrayOf(
        Category(0, R.string.car, R.color.cyan),
        Category(1, R.string.food, R.color.green),
        Category(2, R.string.clothes, R.color.red),
        Category(3, R.string.phone, R.color.yellow),
        Category(4, R.string.salary, R.color.violet),
        Category(5, R.string.other, R.color.colorBlack)
)

val INITIAL_ACCOUNTS = arrayOf(
        Account("Cash", R.drawable.ic_cash),
        Account("Credit card", R.drawable.ic_credit_card),
        Account("Bank", R.drawable.ic_bank)
)

val INITIAL_TRANSACTIONS = arrayOf(
        Transaction(EXPENSE, BigDecimal(10), 1, 1, 1, Date()),
        Transaction(INCOME, BigDecimal(20), 1, 3, 1, Date()),
        Transaction(EXPENSE, BigDecimal(13), 1, 2, 2, Date()),
        Transaction(EXPENSE, BigDecimal(2), 1, 1, 2, Date()),
        Transaction(INCOME, BigDecimal(300), 1, 4, 3, Date())
)


