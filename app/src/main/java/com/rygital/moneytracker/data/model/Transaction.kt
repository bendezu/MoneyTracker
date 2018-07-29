package com.rygital.moneytracker.data.model

import java.math.BigDecimal

data class Transaction(val transactionType: TransactionType, val value: BigDecimal, val currency: Currency)