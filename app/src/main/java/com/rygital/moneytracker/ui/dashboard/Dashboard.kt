package com.rygital.moneytracker.ui.dashboard

import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.base.MvpPresenter
import com.rygital.moneytracker.ui.base.MvpView
import java.math.BigDecimal

interface Dashboard {
    interface View: MvpView {
        fun showPrimaryTotalBalance(value: BigDecimal, symbol: Char)
        fun showSecondaryTotalBalance(value: BigDecimal, symbol: Char)

        fun showAccounts(data: List<AccountPagerItem>)

        fun showCategories(categoryList: List<ChartItem>, totalExpenses: BigDecimal, symbol: Char)

    }

    @FragmentScope
    interface Presenter<in V: View>: MvpPresenter<V> {
        fun loadData()
    }
}