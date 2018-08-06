package com.rygital.moneytracker.injection.modules

import android.content.Context
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.data.remote.CurrencyApi
import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.components.activity.HomeActivityComponent
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.AddTransactionFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.SettingsFragmentComponent
import com.rygital.moneytracker.ui.about.AboutFragment
import com.rygital.moneytracker.ui.dashboard.DashboardFragment
import com.rygital.moneytracker.ui.home.HomeActivity
import com.rygital.moneytracker.ui.settings.SettingsFragment
import com.rygital.moneytracker.ui.transaction.AddTransactionFragment
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import com.rygital.moneytracker.utils.rx.SchedulerProviderImp
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import java.io.File
import javax.inject.Singleton

@Module(subcomponents = [ HomeActivityComponent::class, AboutFragmentComponent::class,
    DashboardFragmentComponent::class, SettingsFragmentComponent::class, AddTransactionFragmentComponent::class ])
class ApplicationModule(private val context: Context,
                        private val cacheFile: File) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideFinanceDatabase(context: Context) = FinanceDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi = CurrencyApi.Creator.createSlaApi(cacheFile)

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImp()

    @Provides
    @IntoMap
    @ClassKey(HomeActivity::class)
    fun provideHomeActivityBuilder(builder: HomeActivityComponent.Builder)
            : ComponentBuilder<*, *> = builder

    @Provides
    @IntoMap
    @ClassKey(DashboardFragment::class)
    fun provideDashboardFragmentBuilder(builder: DashboardFragmentComponent.Builder)
            : ComponentBuilder<*, *> = builder

    @Provides
    @IntoMap
    @ClassKey(SettingsFragment::class)
    fun provideSettingsFragmentBuilder(builder: SettingsFragmentComponent.Builder)
            : ComponentBuilder<*, *> = builder

    @Provides
    @IntoMap
    @ClassKey(AboutFragment::class)
    fun provideAboutFragmentBuilder(builder: AboutFragmentComponent.Builder)
            : ComponentBuilder<*, *> = builder

    @Provides
    @IntoMap
    @ClassKey(AddTransactionFragment::class)
    fun provideAddTransactionBuilder(builder: AddTransactionFragmentComponent.Builder)
            : ComponentBuilder<*, *> = builder
}