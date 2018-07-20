package com.rygital.moneytracker.ui.base

abstract class BasePresenter<V: MvpView>: MvpPresenter<V> {

    private var view: V? = null

    override fun attachView(mvpView: V) {
        this.view = mvpView
    }

    override fun detachView() {
        this.view = null
    }

    protected fun getView(): V? {
        return view
    }

}