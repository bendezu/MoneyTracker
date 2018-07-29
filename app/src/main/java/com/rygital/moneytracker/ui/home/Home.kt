package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.injection.ActivityScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import javax.inject.Singleton

interface Home {
    interface View: MvpView {
        fun showDashboardFragment()
        fun showSettingsFragment()
        fun showAboutFragment()
    }

    @ActivityScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun openDashboardFragment()
        fun openSettingsFragment()
        fun openAboutFragment()
    }
}