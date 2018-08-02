package com.rygital.moneytracker.ui.settings

import android.content.Context
import com.rygital.moneytracker.PREF_KEY_PRIMARY_CURRENCY
import com.rygital.moneytracker.PREF_KEY_SECONDARY_CURRENCY
import com.rygital.moneytracker.PREF_NAME
import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.ui.base.BasePresenter
import javax.inject.Inject

class SettingsPresenter<V: Settings.View> @Inject constructor(private val context: Context): BasePresenter<V>(), Settings.Presenter<V> {

    override fun initSpinners() {
        val currencies = Currency.values().asList()

        val primaryCurrencyId = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(PREF_KEY_PRIMARY_CURRENCY, 0)
        val secondaryCurrencyId = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(PREF_KEY_SECONDARY_CURRENCY, 0)

        view?.initPrimaryCurrencySpinner(currencies, primaryCurrencyId)
        view?.initSecondaryCurrencySpinner(currencies, secondaryCurrencyId)
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