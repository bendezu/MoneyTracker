package com.rygital.moneytracker.ui.transaction

import android.content.Context
import com.rygital.moneytracker.*
import com.rygital.moneytracker.data.model.database.*
import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.Function3
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread

class AddTransactionPresenter<V: AddTransaction.View> @Inject constructor(private val schedulerProvider: SchedulerProvider,
                                                                          private val context: Context)
    : BasePresenter<V>(), AddTransaction.Presenter<V> {

    override fun initNewTransaction() {

        val database = FinanceDatabase.getInstance(context)
        if (database != null) {
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
        } else {
            Timber.e("Can't load database")
        }

    }

    override fun addNewTransaction(isIncome: Boolean, amount: String, currencyId: Int, categoryId: Int, accountId: Int) {

        val database = FinanceDatabase.getInstance(context)
        if (database != null) {

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
            view?.close()
        } else {
            Timber.e("Can't load database")
        }

    }

}