package com.rygital.moneytracker.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RateResponse(@SerializedName("disclaimer")
                   @Expose
                   val disclaimer: String,
                   @SerializedName("license")
                   @Expose
                   val license: String,
                   @SerializedName("timestamp")
                   @Expose
                   val timestamp: Long,
                   @SerializedName("base")
                   @Expose
                   val base: String,
                   @SerializedName("rates")
                   @Expose
                   val usdBasedRates: UsdBasedRates)
