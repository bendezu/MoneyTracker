package com.rygital.moneytracker.ui.addAccount

import com.rygital.moneytracker.data.model.database.Account
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject
import kotlin.concurrent.thread

class AddAccountPresenter<V: AddAccount.View> @Inject constructor(private val database: FinanceDatabase)
    : BasePresenter<V>(), AddAccount.Presenter<V> {

    override fun addAccount(name: String, icon: Int) {
        thread {
            database.accountDao().insert(Account(name, icon))
        }
    }

}