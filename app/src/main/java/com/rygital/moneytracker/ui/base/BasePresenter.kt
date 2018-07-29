package com.rygital.moneytracker.ui.base

import timber.log.Timber

abstract class BasePresenter<V: MvpView>: MvpPresenter<V> {

    init {
        Timber.i("create new Presenter")
    }

    protected var view: V? = null
        private set

    override fun attachView(mvpView: V) {
        this.view = mvpView
    }

    override fun detachView() {
        this.view = null
    }
}