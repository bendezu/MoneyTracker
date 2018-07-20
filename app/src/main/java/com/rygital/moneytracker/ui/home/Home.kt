package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import javax.inject.Singleton

interface Home {
    interface View: MvpView {
        fun showMoneyInRoubles(money: Float)
        fun showMoneyInDollars(money: Float)
    }

    @Singleton
    interface Presenter<in V: View>: MvpPresenter<V> {

    }
}