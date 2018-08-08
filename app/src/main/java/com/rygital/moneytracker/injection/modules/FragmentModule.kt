package com.rygital.moneytracker.injection.modules

import com.rygital.moneytracker.injection.base.MyModule
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.about.About
import com.rygital.moneytracker.ui.about.AboutPresenter
import com.rygital.moneytracker.ui.account.Account
import com.rygital.moneytracker.ui.account.AccountPresenter
import com.rygital.moneytracker.ui.account.TransactionsAdapter
import com.rygital.moneytracker.ui.dashboard.AccountPagerItem
import com.rygital.moneytracker.ui.dashboard.CategoriesAdapter
import com.rygital.moneytracker.ui.dashboard.Dashboard
import com.rygital.moneytracker.ui.dashboard.DashboardPresenter
import com.rygital.moneytracker.ui.settings.Settings
import com.rygital.moneytracker.ui.settings.SettingsPresenter
import com.rygital.moneytracker.ui.transaction.AddTransaction
import com.rygital.moneytracker.ui.transaction.AddTransactionPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule: MyModule {

    @Provides
    @FragmentScope
    fun provideCategoriesAdapter(): CategoriesAdapter = CategoriesAdapter()

    @Provides
    @FragmentScope
    fun provideTransactionsAdapter(): TransactionsAdapter = TransactionsAdapter()

    @Provides
    @FragmentScope
    fun provideDashboardPresenter(presenter: DashboardPresenter<Dashboard.View>): Dashboard.Presenter<Dashboard.View>
            = presenter

    @Provides
    @FragmentScope
    fun provideSettingsPresenter(presenter: SettingsPresenter<Settings.View>): Settings.Presenter<Settings.View>
            = presenter

    @Provides
    @FragmentScope
    fun provideAboutPresenter(presenter: AboutPresenter<About.View>): About.Presenter<About.View>
            = presenter

    @Provides
    @FragmentScope
    fun provideAddTransactionPresenter(presenter: AddTransactionPresenter<AddTransaction.View>)
            : AddTransaction.Presenter<AddTransaction.View> = presenter

    @Provides
    @FragmentScope
    fun provideAccountPresenter(presenter: AccountPresenter<Account.View>)
            : Account.Presenter<Account.View> = presenter
}