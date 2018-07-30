package com.rygital.moneytracker.utils.calculator

import com.rygital.moneytracker.data.model.UsdBasedRates
import com.rygital.moneytracker.data.remote.CurrencyApi
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRatesManager @Inject constructor(private val currencyApi: CurrencyApi) {

    private var usdBasedRates: UsdBasedRates? = null
    private var lastUpdateTime: Long = 0

    fun getRates(): Observable<UsdBasedRates> {
        val currentTime: Long = Calendar.getInstance().timeInMillis / 1000
        Timber.i("current time %s, last update time %s", currentTime, lastUpdateTime)

        return if (usdBasedRates != null && currentTime - lastUpdateTime < 60 * 60 * 1000) {
            Timber.i("load from local storage")
            Observable.just(usdBasedRates!!)
        } else {
            Timber.i("load from server")
            loadRates()
        }
    }

    private fun loadRates(): Observable<UsdBasedRates> {
        return currencyApi.getCurrencyRates("ce9f17c314c840ef8b5d8fd6d472bc4c")
                .doOnNext({it ->
                    lastUpdateTime = it.timestamp
                    usdBasedRates = it.usdBasedRates
                })
                .flatMap { Observable.just(it.usdBasedRates) }
    }
}