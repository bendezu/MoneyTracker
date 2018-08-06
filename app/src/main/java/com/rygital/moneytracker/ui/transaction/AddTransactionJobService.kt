package com.rygital.moneytracker.ui.transaction

import android.app.job.JobParameters
import android.app.job.JobService
import com.rygital.moneytracker.EXPENSE
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.data.model.database.Transaction
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.thread

const val ADD_TRANSACTION_JOB_ID = 101

const val ARG_TYPE = "type"
const val ARG_AMOUNT = "amount"
const val ARG_CURRENCY_ID = "currency_id"
const val ARG_CATEGORY_ID = "category_id"
const val ARG_ACCOUNT_ID = "account_id"

class AddTransactionJobService: JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("Start job: AddTransactionJobService")
        thread {

            val bundle = params?.extras
            if (bundle == null) {
                jobFinished(params, false)
                return@thread
            }
            val type = bundle.getInt(ARG_TYPE, EXPENSE)
            val amount = bundle.getString(ARG_AMOUNT)
            val currencyId = bundle.getInt(ARG_CURRENCY_ID, 0)
            val categoryId = bundle.getInt(ARG_CATEGORY_ID, 0)
            val accountId = bundle.getInt(ARG_ACCOUNT_ID, 0)

            val bigDecimalAmount: BigDecimal = try {
                if (BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0) throw NumberFormatException()
                else BigDecimal(amount)
            } catch (e: NumberFormatException) {
                jobFinished(params, false)
                return@thread
            }

            val transaction = Transaction(type, bigDecimalAmount,
                    currencyId.toLong(), categoryId.toLong(), accountId.toLong(), Date())

            FinanceDatabase.getInstance(applicationContext)?.transactionDao()?.insert(transaction)

            jobFinished(params, false)
        }
        return true
    }

}