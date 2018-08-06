package com.rygital.moneytracker.ui.settings

import android.content.Context
import com.rygital.moneytracker.PREF_KEY_PRIMARY_CURRENCY
import com.rygital.moneytracker.PREF_KEY_SECONDARY_CURRENCY
import com.rygital.moneytracker.PREF_NAME
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.ui.base.BasePresenter
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class SettingsPresenter<V: Settings.View> @Inject constructor(private val context: Context,
                                                              private val schedulerProvider: SchedulerProvider,
                                                              private val database: FinanceDatabase)
    : BasePresenter<V>(), Settings.Presenter<V> {

    override fun initSpinners() {

        val primaryCurrencyId = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(PREF_KEY_PRIMARY_CURRENCY, 0)
        val secondaryCurrencyId = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(PREF_KEY_SECONDARY_CURRENCY, 0)

        addDisposable(database.currencyDao().getAllRx()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe{ currencies ->
                    view?.initPrimaryCurrencySpinner(currencies, primaryCurrencyId)
                    view?.initSecondaryCurrencySpinner(currencies, secondaryCurrencyId)
                })
    }

    override fun savePrimaryCurrency(currencyId: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt(PREF_KEY_PRIMARY_CURRENCY, currencyId).apply()
    }

    override fun saveSecondaryCurrency(currencyId: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt(PREF_KEY_SECONDARY_CURRENCY, currencyId).apply()
    }
}