package com.rygital.moneytracker.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BasePresenter<V: MvpView>: MvpPresenter<V> {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected var view: V? = null
        private set

    init {
        Timber.i("create new Presenter")
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun attachView(mvpView: V) {
        this.view = mvpView
    }

    override fun detachView() {
        compositeDisposable.clear()
        this.view = null
    }
}