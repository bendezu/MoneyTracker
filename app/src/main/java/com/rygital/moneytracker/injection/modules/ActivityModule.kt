package com.rygital.moneytracker.injection.modules

import com.rygital.moneytracker.ui.about.About
import com.rygital.moneytracker.ui.about.AboutPresenter
import com.rygital.moneytracker.ui.dashboard.Dashboard
import com.rygital.moneytracker.ui.dashboard.DashboardPresenter
import com.rygital.moneytracker.ui.home.Home
import com.rygital.moneytracker.ui.home.HomePresenter
import com.rygital.moneytracker.ui.settings.Settings
import com.rygital.moneytracker.ui.settings.SettingsPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule {
    @Provides
    @Singleton
    fun provideHomePresenter(presenter: HomePresenter<Home.View>): Home.Presenter<Home.View> = presenter

    @Provides
    fun provideDashboardPresenter(presenter: DashboardPresenter<Dashboard.View>): Dashboard.Presenter<Dashboard.View>
            = presenter

    @Provides
    fun provideSettingsPresenter(presenter: SettingsPresenter<Settings.View>): Settings.Presenter<Settings.View>
            = presenter

    @Provides
    fun provideAboutPresenter(presenter: AboutPresenter<About.View>): About.Presenter<About.View> = presenter
}