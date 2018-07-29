package com.rygital.moneytracker.ui.settings

import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface Settings {
    interface View: MvpView {

    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {

    }
}