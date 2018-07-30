package com.rygital.moneytracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import com.rygital.moneytracker.utils.formatMoney
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject

class DashboardFragment: BaseFragment(), Dashboard.View {
    companion object {
        const val TAG: String = "DashboardFragment"
    }

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

        (App.getApp(context!!).componentsHolder?.getComponent(javaClass) as DashboardFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        presenter.loadData()
    }

    override fun showMoneyInRUB(value: Double) {
        tvRoubles?.text = String.format("₽ %s", formatMoney(value))
    }

    override fun showMoneyInUSD(value: Double) {
        tvDollars?.text = String.format("$ %s", formatMoney(value))
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

        super.onDestroyView()

        if (isRemoving) App.getApp(context!!).componentsHolder?.releaseComponent(javaClass)
    }
}