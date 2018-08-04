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

val INITIAL_CURRENCIES = arrayOf(
        Currency(0, "USD", '$', 1.0),
        Currency(1,"EUR", '\u20AC',0.86),
        Currency(2,"RUB", '\u20BD', 63.0)
)

val INITIAL_CATEGORIES = arrayOf(
        Category(0, R.string.car, R.color.cyan),
        Category(1, R.string.food, R.color.green),
        Category(2, R.string.clothes, R.color.red),
        Category(3, R.string.other, R.color.violet)
)

val INITIAL_ACCOUNTS = arrayOf(
        Account(0, R.string.cash, R.drawable.ic_monetization),
        Account(1, R.string.bank_card, R.drawable.ic_credit_card)
)

val INITIAL_TRANSACTIONS = arrayOf(
        Transaction(EXPENSE, BigDecimal(1), 1, 1, 0, Date()),
        Transaction(INCOME, BigDecimal(2), 1, 3, 0, Date()),
        Transaction(EXPENSE, BigDecimal(3), 1, 2, 1, Date()),
        Transaction(EXPENSE, BigDecimal(1), 1, 1, 1, Date())
)


