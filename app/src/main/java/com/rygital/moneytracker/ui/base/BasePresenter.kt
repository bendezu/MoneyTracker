package com.rygital.moneytracker.ui.base

abstract class BasePresenter<V: MvpView>: MvpPresenter<V> {

    protected var view: V? = null
        private set

    override fun attachView(mvpView: V) {
        this.view = mvpView
    }

    override fun detachView() {
        this.view = null
    }
}