package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.injection.scopes.ActivityScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface Home {
    interface View: MvpView {
        fun showDashboardFragment()
        fun showSettingsFragment()
        fun showAboutFragment()
        fun showAddTransactionFragment(accountId: Int? = null, transaction: DetailedTransaction? = null)
        fun showAccountFragment(accountId: Int)
        fun showAddAccountFragment()
    }

    @ActivityScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun openDashboardFragment()
        fun openSettingsFragment()
        fun openAboutFragment()
        fun openAddTransactionFragment(accountId: Int? = null, transaction: DetailedTransaction? = null)
        fun openAccountFragment(accountId: Int)
        fun openAddAccountFragment()
    }
}