package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import javax.inject.Inject

class DashboardPresenter<V: Dashboard.View> @Inject constructor(): BasePresenter<V>(), Dashboard.Presenter<V> {

}