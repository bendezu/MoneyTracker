package com.rygital.moneytracker.injection.components

import com.rygital.moneytracker.injection.components.activity.HomeActivityComponent
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.SettingsFragmentComponent
import com.rygital.moneytracker.injection.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
    fun createHomeActivityComponent(): HomeActivityComponent

    fun createDashboardFragmentComponent(): DashboardFragmentComponent
    fun createSettingsFragmentComponent(): SettingsFragmentComponent
    fun createAboutFragmentComponent(): AboutFragmentComponent
}