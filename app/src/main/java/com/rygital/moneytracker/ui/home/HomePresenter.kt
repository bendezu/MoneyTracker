package com.rygital.moneytracker.ui.home

import android.util.Log
import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class HomePresenter<V: Home.View> @Inject constructor() : BasePresenter<V>(), Home.Presenter<V> {

    init {
        Log.d("TAG", "AZAAZAZA")
    }

    override fun openDashboardFragment() {
        view?.showDashboardFragment()
    }

    override fun openSettingsFragment() {
        view?.showSettingsFragment()
    }

    override fun openAboutFragment() {
        view?.showAboutFragment()
    }
}