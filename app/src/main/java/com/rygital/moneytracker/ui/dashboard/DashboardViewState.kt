package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.data.model.Category
import java.math.BigDecimal

class DashboardViewState(val totalSumInUSD: BigDecimal,
                         val totalSumInRub: BigDecimal,
                         val categories: List<Category>,
                         val totalExpenses: BigDecimal)