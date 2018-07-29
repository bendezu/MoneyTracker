package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface Dashboard {
    interface View: MvpView {
        fun showMoneyInRUB(value: Double)
        fun showMoneyInUSD(value: Double)
    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun loadData()
    }
}