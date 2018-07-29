package com.rygital.moneytracker.injection.modules

import android.content.Context
import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.components.activity.HomeActivityComponent
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.SettingsFragmentComponent
import com.rygital.moneytracker.ui.about.AboutFragment
import com.rygital.moneytracker.ui.dashboard.DashboardFragment
import com.rygital.moneytracker.ui.home.HomeActivity
import com.rygital.moneytracker.ui.settings.SettingsFragment
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Provides
    @IntoMap
    @ClassKey(HomeActivity::class)
    fun provideHomeActivityBuilder(builder: HomeActivityComponent.Builder)
            : ComponentBuilder<HomeActivityComponent, ActivityModule> = builder

    @Provides
    @IntoMap
    @ClassKey(DashboardFragment::class)
    fun provideDashboardFragmentBuilder(builder: DashboardFragmentComponent.Builder)
            : ComponentBuilder<DashboardFragmentComponent, FragmentModule> = builder

    @Provides
    @IntoMap
    @ClassKey(SettingsFragment::class)
    fun provideSettingsFragmentBuilder(builder: SettingsFragmentComponent.Builder)
            : ComponentBuilder<SettingsFragmentComponent, FragmentModule> = builder

    @Provides
    @IntoMap
    @ClassKey(AboutFragment::class)
    fun provideAboutFragmentBuilder(builder: AboutFragmentComponent.Builder)
            : ComponentBuilder<AboutFragmentComponent, FragmentModule> = builder
}