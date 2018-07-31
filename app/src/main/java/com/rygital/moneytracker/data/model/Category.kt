package com.rygital.moneytracker.data.model

import android.support.annotation.ColorRes
import com.rygital.moneytracker.R
import java.math.BigDecimal

data class Category(val id: Long,
               var title: String,
               var plan: BigDecimal,
               @ColorRes  var color: Int = R.color.orange,
               var fact: BigDecimal = BigDecimal("0.0"),
               var description: String = "")