package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import com.rygital.moneytracker.utils.formatMoney
import javax.inject.Inject

class DashboardFragment: BaseFragment(), Dashboard.View {
    companion object {
        const val TAG: String = "DashboardFragment"
    }

    private var tvRoubles: TextView? = null
    private var tvDollars: TextView? = null

    @Inject @JvmSuppressWildcards lateinit var presenter: Dashboard.Presenter<Dashboard.View>

    private var onMenuClickListener: OnMenuClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }

        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        App.instance?.applicationComponent?.inject(this)

        tvRoubles = v.findViewById(R.id.tvRoubles)
        tvDollars = v.findViewById(R.id.tvDollars)

        presenter.attachView(this)
        init()

        return v
    }

    private fun init() {
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun showMoneyInRoubles(money: Float) {
        tvRoubles?.text = String.format("₽ %s", formatMoney(money))
    }

    override fun showMoneyInDollars(money: Float) {
        tvDollars?.text = String.format("$ %s", formatMoney(money))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.selSettings -> onMenuClickListener?.openSettingsScreen()
            R.id.selAbout -> onMenuClickListener?.openAboutScreen()
        }

        return false
    }

    override fun onDestroyView() {
        presenter.detachView()
        tvRoubles = null
        tvDollars = null

        super.onDestroyView()
    }
}