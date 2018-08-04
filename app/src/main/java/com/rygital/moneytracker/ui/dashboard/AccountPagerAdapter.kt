package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.R
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.account_item.view.*
import java.math.BigDecimal
import javax.inject.Inject

class AccountPagerAdapter(val data: List<AccountPagerItem>, val context: Context?) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.account_item, container, false)
        view.accountIcon.setImageResource(data[position].icon)
        view.accountLabel.setText(data[position].label)
        val primary = "${data[position].primaryCurrencySymbol} ${formatMoney(data[position].primaryBalance)}"
        view.accountPrimaryBalance.text = primary
        val secondary = "${data[position].secondaryCurrencySymbol} ${formatMoney(data[position].secondaryBalance)}"
        view.accountSecondaryBalance.text = secondary

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun getCount(): Int = data.count()

}