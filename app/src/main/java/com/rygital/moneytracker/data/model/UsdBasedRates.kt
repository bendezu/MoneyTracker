package com.rygital.moneytracker.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class UsdBasedRates(@SerializedName("RUB")
                    @Expose
                    val rub: BigDecimal,
                    @SerializedName("EUR")
                    @Expose
                    val eur: BigDecimal) {

    fun getRateByCurrency(currency: Currency): BigDecimal {
        return when(currency) {
            Currency.RUB -> rub
            Currency.EUR -> eur
            else -> BigDecimal.ONE
        }
    }
}