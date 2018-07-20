package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class HomePresenter<V: Home.View> @Inject constructor() : BasePresenter<V>(), Home.Presenter<V>