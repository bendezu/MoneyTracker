package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(): BasePresenter<V>(), Dashboard.Presenter<V> {

    companion object {
        const val TEMPORARY_HARDCODED_RUB_USD_RATE: Double = 63.46431
        const val TEMPORARY_HARDCODED_BALANCE_IN_USD: Double = 217.69
    }

    override fun loadData() {
        view?.showMoneyInUSD(TEMPORARY_HARDCODED_BALANCE_IN_USD)
        view?.showMoneyInRUB(TEMPORARY_HARDCODED_BALANCE_IN_USD * TEMPORARY_HARDCODED_RUB_USD_RATE)
    }
}