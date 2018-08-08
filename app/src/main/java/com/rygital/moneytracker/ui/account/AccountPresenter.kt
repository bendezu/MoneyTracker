package com.rygital.moneytracker.ui.account

import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject
import kotlin.concurrent.thread

class AccountPresenter<V: Account.View> @Inject constructor(private val schedulerProvider: SchedulerProvider,
                                                            private val database: FinanceDatabase)
    : BasePresenter<V>(), Account.Presenter<V> {

    override fun loadData(accountId: Int) {
        addDisposable(database.transactionDao().getDetailedTransactionsForAccount(accountId.toLong())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ transactions ->
                    view?.showTransactions(transactions)
                }, { err ->
                    Timber.e(err)
                    err.printStackTrace()
                })
        )
    }

    override fun deleteTransaction(transactionId: Long) {
        thread {
            database.transactionDao().delete(transactionId)
        }
    }
}