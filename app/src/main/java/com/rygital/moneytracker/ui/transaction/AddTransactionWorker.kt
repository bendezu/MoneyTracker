package com.rygital.moneytracker.ui.transaction

import androidx.work.Worker
import com.rygital.moneytracker.EXPENSE
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.data.model.database.Transaction
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

const val ARG_TYPE = "type"
const val ARG_AMOUNT = "amount"
const val ARG_CURRENCY_ID = "currency_id"
const val ARG_CATEGORY_ID = "category_id"
const val ARG_ACCOUNT_ID = "account_id"

class AddTransactionWorker : Worker() {
    override fun doWork(): Result {
        Timber.d("Start work: AddTransactionWorker")

        val type = inputData.getInt(ARG_TYPE, EXPENSE)
        val amount = inputData.getString(ARG_AMOUNT)
        val currencyId = inputData.getInt(ARG_CURRENCY_ID, 0)
        val categoryId = inputData.getInt(ARG_CATEGORY_ID, 0)
        val accountId = inputData.getInt(ARG_ACCOUNT_ID, 0)

        val bigDecimalAmount: BigDecimal = try {
            if (BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0) throw NumberFormatException()
            else BigDecimal(amount)
        } catch (e: NumberFormatException) {
            return Result.FAILURE
        }

        val transaction = Transaction(type, bigDecimalAmount,
                currencyId.toLong(), categoryId.toLong(), accountId.toLong(), Date())

        FinanceDatabase.getInstance(applicationContext).transactionDao().insert(transaction)

        return Result.SUCCESS
    }

}