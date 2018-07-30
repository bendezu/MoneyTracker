package com.rygital.moneytracker.data.model

import java.math.BigDecimal
import java.util.*

data class Transaction(val transactionType: TransactionType,
                       val value: BigDecimal,
                       val currency: Currency,
                       val accountId: Long,
                       var categoryId: Long,
                       var dateTime: Date,
                       var comment: String = "")