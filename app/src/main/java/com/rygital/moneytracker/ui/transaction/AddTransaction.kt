package com.rygital.moneytracker.ui.transaction

import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface AddTransaction {
    interface View: MvpView {
        fun setAccountAdapter(list: List<String>)
        fun setCategoryAdapter(list: List<String>)
        fun setCurrencyAdapter(list: List<String>, initial: Int)

        fun close()
    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun initNewTransaction()
        fun addNewTransaction(isIncome: Boolean, amount: String, currencyId: Int, categoryId: Int, accountId: Int)
        fun addPeriodicTransaction(isIncome: Boolean, amount: String, currencyId: Int, categoryId: Int, accountId: Int,
                                   interval: Int, intervalId: Int)
    }
}