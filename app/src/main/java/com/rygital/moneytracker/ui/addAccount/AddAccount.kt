package com.rygital.moneytracker.ui.addAccount

import android.support.annotation.DrawableRes
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView

interface AddAccount {
    interface View : MvpView {

    }

    interface Presenter<in V: AddAccount.View> : MvpPresenter<V> {

        fun addAccount(name: String, @DrawableRes icon: Int)
    }
}