package com.rygital.moneytracker.ui.transaction

import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.local.DatabaseHelper
import com.rygital.moneytracker.data.model.*
import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class AddTransactionPresenter<V: AddTransaction.View> @Inject constructor(private val databaseHelper: DatabaseHelper,
                                                                          private val schedulerProvider: SchedulerProvider)
    : BasePresenter<V>(), AddTransaction.Presenter<V> {

    private val compositeDisposable = CompositeDisposable()

    private var accounts: List<Account> = listOf()
    private var categories: List<Category> = listOf()

    override fun initNewTransaction() {
        compositeDisposable.add(
                Observable
                        .zip(databaseHelper.getAccounts(), databaseHelper.getCategories(),
                                BiFunction { accounts: List<Account>, categories: List<Category> ->
                                    this.accounts = accounts
                                    this.categories = categories
                                    AddTransactionViewState(
                                            accounts.map { it.title },
                                            categories.map { it.title })
                                })
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(object: DisposableObserver<AddTransactionViewState>() {
                            override fun onComplete() {

                            }

                            override fun onNext(viewState: AddTransactionViewState) {
                                view?.setAccountAdapter(viewState.accounts)
                                view?.setCategoryAdapter(viewState.categories)
                                view?.setCurrencyAdapter(Currency.values().toList())
                            }

                            override fun onError(e: Throwable) {
                                Timber.e(e)
                                e.printStackTrace()
                            }
                        })
        )
    }

    override fun addNewTransaction(accountPos: Int, categoryPos: Int, currency: Currency, value: String) {
        val bigDecimalValue: BigDecimal = try {
            if (BigDecimal(value).compareTo(BigDecimal.ZERO) == 0) throw NumberFormatException()
            else BigDecimal(value)
        } catch (e: NumberFormatException) {
            view?.showMessage(R.string.incorrect_value)
            return
        }

        val transaction = Transaction(
                TransactionType.CREDIT,
                bigDecimalValue,
                currency,
                accounts[accountPos].id,
                categories[categoryPos].id,
                Calendar.getInstance().time
        )

        databaseHelper.addTransaction(transaction)
        view?.close()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }
}