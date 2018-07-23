package com.rygital.moneytracker.ui.base

import android.support.annotation.StringRes

interface MvpView {
    fun showMessage(@StringRes resId: Int)
    fun showMessage(message: String)
}