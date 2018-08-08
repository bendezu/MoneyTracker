package com.rygital.moneytracker.ui.account

import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface Account {

    interface View: MvpView {
        fun showTransactions(transactions: List<DetailedTransaction>)
    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun loadData(accountId: Int)
        fun deleteTransaction(transactionId: Long)
    }
}