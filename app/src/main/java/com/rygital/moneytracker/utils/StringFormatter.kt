package com.rygital.moneytracker.utils

import java.math.BigDecimal

fun formatMoney(value: BigDecimal) = String.format("%.2f", value)