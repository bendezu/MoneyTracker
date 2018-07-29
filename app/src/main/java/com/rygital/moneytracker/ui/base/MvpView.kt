package com.rygital.moneytracker.ui.base

import android.support.annotation.StringRes

interface MvpView {
    fun setActionBarTitle(@StringRes resId: Int)
    fun setActionBarTitle(message: String)

    fun showMessage(@StringRes resId: Int)
    fun showMessage(message: String)
}