package com.rygital.moneytracker.ui.home

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.database.DetailedTransaction
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
            if(resources.getBoolean(R.bool.isTwoPaneMode)) {
                presenter.openAccountFragment(0)
            }
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
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.container, DashboardFragment(), DashboardFragment.TAG)
                .commit()
    }

    override fun showSettingsFragment() {
        changeFragment(SettingsFragment(), R.id.container, SettingsFragment.TAG,
                SETTINGS_TRANSACTION, FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }

    override fun showAboutFragment() {
        changeFragment(AboutFragment(), R.id.container, AboutFragment.TAG,
                ABOUT_TRANSACTION, FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }

    override fun showAddTransactionFragment(accountId: Int?, transaction: DetailedTransaction?) {
        Timber.i("transaction fragment")
        changeFragment(AddTransactionFragment.newInstance(accountId, transaction), R.id.container,
                AddTransactionFragment.TAG, ADD_TRANSACTION_TRANSACTION, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
    }

    override fun showAccountFragment(accountId: Int) {
        if (resources.getBoolean(R.bool.isTwoPaneMode)) {
            supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .replace(R.id.container_right, AccountFragment.newInstance(accountId), AccountFragment.TAG)
                    .commit()
        } else {
            changeFragment(AccountFragment.newInstance(accountId), R.id.container,
                    AccountFragment.TAG, ACCOUNT_TRANSACTION, FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }

    override fun showAddAccountFragment() {
        changeFragment(AddAccountFragment(), R.id.container, AddAccountFragment.TAG,
                ADD_ACCOUNT_TRANSACTION, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
    }

    private fun changeFragment(fragment: BaseFragment, containerId: Int, tag: String,
                               transactionName: String, transition: Int) {
        supportFragmentManager
                .beginTransaction()
                .setTransition(transition)
                .replace(containerId, fragment, tag)
                .addToBackStack(transactionName)
                .commit()
    }

    override fun openAboutScreen() {
        presenter.openAboutFragment()
    }

    override fun openSettingsScreen() {
        presenter.openSettingsFragment()
    }

    override fun openAddTransactionScreen(accountId: Int?, transaction: DetailedTransaction?) {
        presenter.openAddTransactionFragment(accountId, transaction)
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