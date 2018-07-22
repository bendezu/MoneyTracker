package com.rygital.moneytracker.data.model

data class Transaction(val transactionType: TransactionType, val value: Double, val currency: Currency)