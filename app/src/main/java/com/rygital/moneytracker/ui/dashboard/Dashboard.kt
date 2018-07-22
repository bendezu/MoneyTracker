package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import javax.inject.Singleton

interface Dashboard {
    interface View: MvpView {
        fun showMoneyInRUB(money: Float)
        fun showMoneyInUSD(money: Float)
    }

    interface Presenter<in V: View>: MvpPresenter<V> {
        fun loadData()
    }
}