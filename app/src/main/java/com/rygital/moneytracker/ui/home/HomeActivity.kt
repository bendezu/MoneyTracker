package com.rygital.moneytracker.ui.home

import android.os.Bundle
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.activity.HomeActivityComponent
import com.rygital.moneytracker.ui.about.AboutFragment
import com.rygital.moneytracker.ui.account.AccountFragment
import com.rygital.moneytracker.ui.addAccount.AddAccountFragment
import com.rygital.moneytracker.ui.base.BaseActivity
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.dashboard.DashboardFragment
import com.rygital.moneytracker.ui.settings.SettingsFragment
import com.rygital.moneytracker.ui.transaction.AddTransactionFragment
import timber.log.Timber
import javax.inject.Inject

class HomeActivity: BaseActivity(), Home.View, OnMenuClickListener {

    companion object {
        const val SETTINGS_TRANSACTION = "settings_transaction"
        const val ABOUT_TRANSACTION = "about_transaction"
        const val ADD_TRANSACTION_TRANSACTION = "add_transaction_transaction"
        const val ACCOUNT_TRANSACTION = "account_transaction"
        const val ADD_ACCOUNT_TRANSACTION = "add_account_transaction"

    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Home.Presenter<Home.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (App.instance?.componentsHolder?.getComponent(javaClass) as HomeActivityComponent)
                .inject(this)

        setContentView(R.layout.activity_home)

        presenter.attachView(this)
        init()

        if (savedInstanceState == null)
            presenter.openDashboardFragment()
    }

    private fun init() {
        supportFragmentManager.addOnBackStackChangedListener { displayBackButton() }
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

    override fun showAddTransactionFragment(accountId: Int) {
        Timber.i("transaction fragment")
        changeFragment(AddTransactionFragment.newInstance(accountId), AddTransactionFragment.TAG, ADD_TRANSACTION_TRANSACTION)
    }

    override fun showAccountFragment(accountId: Int) {
        changeFragment(AccountFragment.newInstance(accountId), AccountFragment.TAG, ACCOUNT_TRANSACTION)
    }

    override fun showAddAccountFragment() {
        changeFragment(AddAccountFragment(), AddAccountFragment.TAG, ADD_ACCOUNT_TRANSACTION)
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

    override fun openAddTransactionScreen(accountId: Int) {
        presenter.openAddTransactionFragment(accountId)
    }

    override fun openAccountScreen(accountId: Int) {
        presenter.openAccountFragment(accountId)
    }

    override fun openAddAccountScreen() {
        presenter.openAddAccountFragment()
    }

    override fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()

        if (isFinishing) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}