package com.rygital.moneytracker.ui.settings

import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import javax.inject.Singleton

interface Settings {
    interface View: MvpView {

    }

    interface Presenter<in V: View>: MvpPresenter<V> {

    }
}