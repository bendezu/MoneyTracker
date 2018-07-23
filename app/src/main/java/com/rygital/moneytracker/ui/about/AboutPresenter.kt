package com.rygital.moneytracker.ui.about

import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class AboutPresenter<V: About.View> @Inject constructor(): BasePresenter<V>(), About.Presenter<V>