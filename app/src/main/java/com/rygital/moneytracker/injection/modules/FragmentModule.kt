package com.rygital.moneytracker.injection.modules

import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.about.About
import com.rygital.moneytracker.ui.about.AboutPresenter
import com.rygital.moneytracker.ui.dashboard.Dashboard
import com.rygital.moneytracker.ui.dashboard.DashboardPresenter
import com.rygital.moneytracker.ui.settings.Settings
import com.rygital.moneytracker.ui.settings.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

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
}