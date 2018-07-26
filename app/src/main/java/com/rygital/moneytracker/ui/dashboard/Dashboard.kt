package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import java.math.BigDecimal

interface Dashboard {
    interface View: MvpView {
        fun showMoneyInRUB(value: BigDecimal)
        fun showMoneyInUSD(value: BigDecimal)
    }

    interface Presenter<in V: View>: MvpPresenter<V> {
        fun loadData()
    }
}