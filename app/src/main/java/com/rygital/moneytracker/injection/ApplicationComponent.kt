package com.rygital.moneytracker.injection

import com.rygital.moneytracker.injection.modules.ActivityModule
import com.rygital.moneytracker.ui.about.AboutFragment
import com.rygital.moneytracker.ui.dashboard.DashboardFragment
import com.rygital.moneytracker.ui.home.HomeActivity
import com.rygital.moneytracker.ui.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ActivityModule::class)])
interface ApplicationComponent {
    fun inject(activity: HomeActivity)

    fun inject(fragment: DashboardFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: AboutFragment)
}