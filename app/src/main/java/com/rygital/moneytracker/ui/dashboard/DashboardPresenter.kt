package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(): BasePresenter<V>(), Dashboard.Presenter<V> {

    companion object {
        const val TEMPORARY_HARDCODED_RUB_USD_RATE: Float = 63.46431f
        const val TEMPORARY_HARDCODED_BALANCE_IN_USD: Float = 217.69f
    }

    override fun loadData() {
        view?.showMoneyInUSD(TEMPORARY_HARDCODED_BALANCE_IN_USD)
        view?.showMoneyInRUB(TEMPORARY_HARDCODED_BALANCE_IN_USD * TEMPORARY_HARDCODED_RUB_USD_RATE)
    }
}