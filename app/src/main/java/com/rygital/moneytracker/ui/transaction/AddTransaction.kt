package com.rygital.moneytracker.ui.transaction

import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface AddTransaction {
    interface View: MvpView {
        fun setAccountAdapter(list: List<String>)
        fun setCategoryAdapter(list: List<String>)
        fun setCurrencyAdapter(list: List<Currency>)

        fun close()
    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun initNewTransaction()
        fun addNewTransaction(accountPos: Int, categoryPos: Int, currency: Currency, value: String)
    }
}