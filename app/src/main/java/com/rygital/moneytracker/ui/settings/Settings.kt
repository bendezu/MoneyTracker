package com.rygital.moneytracker.ui.settings

import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface Settings {
    interface View: MvpView {
        fun initPrimaryCurrencySpinner(list: List<Currency>, initial: Int)
        fun initSecondaryCurrencySpinner(list: List<Currency>, initial: Int)
    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun initSpinners()
        fun savePrimaryCurrency(currencyId: Int)
        fun saveSecondaryCurrency(currencyId: Int)
    }
}