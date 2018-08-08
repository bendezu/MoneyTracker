package com.rygital.moneytracker.ui.transaction

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import androidx.work.*
import com.rygital.moneytracker.*
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.database.*
import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.Function3
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.thread

class AddTransactionPresenter<V: AddTransaction.View> @Inject constructor(private val schedulerProvider: SchedulerProvider,
                                                                          private val context: Context,
                                                                          private val database: FinanceDatabase)
    : BasePresenter<V>(), AddTransaction.Presenter<V> {

    override fun initNewTransaction() {

        addDisposable(Observable.zip(
                database.currencyDao().getAllRx().toObservable(),
                database.accountDao().getAllRx().toObservable(),
                database.categoryDao().getAllRx().toObservable(),
                Function3 { currencies: List<Currency>, accounts: List<Account>, categories: List<Category> ->
                    val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val primaryCurrencyId = preferences.getInt(PREF_KEY_PRIMARY_CURRENCY, 0)
                    AddTransactionViewState(accounts.map { context.getString(it.label) },
                        categories.map { context.getString(it.label) },
                        currencies.map { it.label }, primaryCurrencyId)
            })
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({viewState: AddTransactionViewState ->

                view?.setAccountAdapter(viewState.accounts)
                view?.setCategoryAdapter(viewState.categories)
                view?.setCurrencyAdapter(viewState.currencies, viewState.initialCurrency)

            }, {e: Throwable ->
                Timber.e(e)
                e.printStackTrace()
            })
        )

    }

    override fun addNewTransaction(isIncome: Boolean, amount: String, currencyId: Int, categoryId: Int, accountId: Int,
                                   saveAsPattern: Boolean) {

        val bigDecimalAmount: BigDecimal = try {
            if (BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0) throw NumberFormatException()
            else BigDecimal(amount)
        } catch (e: NumberFormatException) {
            view?.showMessage(R.string.incorrect_value)
            return
        }

        val type = if (isIncome) INCOME else EXPENSE

        val transaction = Transaction(type, bigDecimalAmount,
                currencyId.toLong(), categoryId.toLong(), accountId.toLong(), Date())

        thread {  database.transactionDao().insert(transaction) }
        if (saveAsPattern) {
            val pattern = Pattern(type, bigDecimalAmount,
                    currencyId.toLong(), categoryId.toLong(), accountId.toLong(), Date())
            thread { database.patternDao().insert(pattern) }
        }
        view?.close()

    }

    override fun addPeriodicTransaction(isIncome: Boolean, amount: String, currencyId: Int, categoryId: Int, accountId: Int,
                                        interval: Int, intervalId: Int) {
        val type = if (isIncome) INCOME else EXPENSE

        try {
            if (BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0) throw NumberFormatException()
        } catch (e: NumberFormatException) {
            Timber.e(e)
            view?.showMessage(R.string.incorrect_value)
            return
        }

        val repeatInterval = interval * when (intervalId) {
            0 -> { 1 } //Day
            1 -> { 7 } //Week
            2 -> { 30 } //Month
            3 -> { 365 } //Year
            else -> throw IllegalArgumentException()
        }.toLong()

        val data = mapOf(
                ARG_TYPE to type,
                ARG_AMOUNT to amount,
                ARG_CURRENCY_ID to currencyId,
                ARG_CATEGORY_ID to categoryId,
                ARG_ACCOUNT_ID to accountId
        ).toWorkData()

        val request = PeriodicWorkRequestBuilder<AddTransactionWorker>(repeatInterval, TimeUnit.DAYS)
                .setInputData(data)
                .build()

        val uniqueName = System.currentTimeMillis()

        WorkManager.getInstance().enqueueUniquePeriodicWork(uniqueName.toString(),
                ExistingPeriodicWorkPolicy.REPLACE, request)

        view?.close()
    }
}