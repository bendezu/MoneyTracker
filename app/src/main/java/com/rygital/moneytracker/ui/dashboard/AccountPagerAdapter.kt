package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.EMO_REGEX
import com.rygital.moneytracker.R
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.account_item.view.*
import java.util.regex.Pattern

class AccountPagerAdapter(val context: Context?,
                          private val presenter: Dashboard.Presenter<Dashboard.View>) : PagerAdapter() {

    var data: List<AccountPagerItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        if (position == data.count()) {
            val view = LayoutInflater.from(context).inflate(R.layout.new_account_item, container, false)
            view.setOnClickListener { presenter.openAddAccountScreen() }
            container.addView(view)
            return view
        }

        val view = LayoutInflater.from(context).inflate(R.layout.account_item, container, false)

        var label = data[position].label
        val matcher = Pattern.compile(EMO_REGEX).matcher(label)
        if (matcher.find()) {
            val emoji = matcher.group()
            if (label.startsWith(emoji)) {
                label = label.substring(emoji.length)
                view.accountIcon.visibility = View.GONE
                view.accountEmoji.text = emoji
            }
        }

        view.accountIcon.setImageResource(data[position].icon)
        view.accountLabel.text = label
        val primary = "${data[position].primaryCurrencySymbol} ${formatMoney(data[position].primaryBalance)}"
        view.accountPrimaryBalance.text = primary
        val secondary = "${data[position].secondaryCurrencySymbol} ${formatMoney(data[position].secondaryBalance)}"
        view.accountSecondaryBalance.text = secondary

        view.setOnClickListener { presenter.openAccountScreen(data[position].id.toInt()) }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun getCount(): Int = if (data.isEmpty()) 0 else data.count() + 1

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}