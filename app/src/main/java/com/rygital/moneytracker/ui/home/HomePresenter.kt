package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class HomePresenter<V: Home.View> @Inject constructor() : BasePresenter<V>(), Home.Presenter<V> {

    override fun openDashboardFragment() {
        view?.showDashboardFragment()
    }

    override fun openSettingsFragment() {
        view?.showSettingsFragment()
    }

    override fun openAboutFragment() {
        view?.showAboutFragment()
    }

    override fun openAddTransactionFragment(accountId: Int?, transaction: DetailedTransaction?) {
        view?.showAddTransactionFragment(accountId, transaction)
    }

    override fun openAccountFragment(accountId: Int) {
        view?.showAccountFragment(accountId)
    }

    override fun openAddAccountFragment() {
        view?.showAddAccountFragment()
    }
}