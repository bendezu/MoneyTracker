package com.rygital.moneytracker.ui.base

interface MvpPresenter<in V: MvpView> {
    fun attachView(mvpView: V)
    fun detachView()
}