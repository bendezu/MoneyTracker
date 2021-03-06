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

    fun getRateByCurrency(currency: String): BigDecimal {
        return when(currency) {
            "RUB" -> rub
            "EUR" -> eur
            else -> BigDecimal.ONE
        }
    }
}