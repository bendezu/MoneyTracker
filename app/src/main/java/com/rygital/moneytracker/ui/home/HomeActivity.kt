package com.rygital.moneytracker.ui.home

import android.os.Bundle
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.ui.about.AboutFragment
import com.rygital.moneytracker.ui.base.BaseActivity
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.dashboard.DashboardFragment
import com.rygital.moneytracker.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity: BaseActivity(), Home.View, OnMenuClickListener {

    companion object {
        const val SETTINGS_TRANSACTION: String = "settings_transaction"
        const val ABOUT_TRANSACTION: String = "about_transaction"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Home.Presenter<Home.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance?.componentsHolder
                ?.getHomeActivityComponent()
                ?.inject(this)

        setContentView(R.layout.activity_home)

        presenter.attachView(this)
        init()

        if (savedInstanceState == null)
            presenter.openDashboardFragment()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        supportFragmentManager.addOnBackStackChangedListener({ displayBackButton() })
        displayBackButton()
    }

    private fun displayBackButton() {
        val showBackButton: Boolean = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
    }

    override fun showDashboardFragment() {
        supportFragmentManager
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.container, DashboardFragment(), DashboardFragment.TAG)
                .commit()
    }

    override fun showSettingsFragment() {
        changeFragment(SettingsFragment(), SettingsFragment.TAG, SETTINGS_TRANSACTION)
    }

    override fun showAboutFragment() {
        changeFragment(AboutFragment(), AboutFragment.TAG, ABOUT_TRANSACTION)
    }

    private fun changeFragment(fragment: BaseFragment, tag: String, transactionName: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(transactionName)
                .commit()
    }

    override fun openAboutScreen() {
        presenter.openAboutFragment()
    }

    override fun openSettingsScreen() {
        presenter.openSettingsFragment()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()

        if (isFinishing) App.instance?.componentsHolder?.releaseHomeActivityComponent()
    }
}