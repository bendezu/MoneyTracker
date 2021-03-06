package com.rygital.moneytracker.ui.dashboard

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.rygital.moneytracker.data.model.database.Currency
import java.math.BigDecimal

class DashboardViewState(val totalPrimarySum: BigDecimal,
                         val totalSecondarySum: BigDecimal,
                         val accountPagerData: List<AccountPagerItem>,
                         val primaryCurrency: Currency,
                         val secondaryCurrency: Currency)

class AccountPagerItem(
        val id: Long,
        @DrawableRes val icon: Int,
        val label: String,
        val primaryBalance: BigDecimal,
        val primaryCurrencySymbol: Char,
        val secondaryBalance: BigDecimal,
        val secondaryCurrencySymbol: Char
)

class ChartItem(@ColorRes val colorRes: Int,
                @StringRes val label: Int,
                val amount: BigDecimal
)