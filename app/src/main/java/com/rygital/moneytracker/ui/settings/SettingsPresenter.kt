package com.rygital.moneytracker.ui.settings

import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class SettingsPresenter<V: Settings.View> @Inject constructor(): BasePresenter<V>(), Settings.Presenter<V>