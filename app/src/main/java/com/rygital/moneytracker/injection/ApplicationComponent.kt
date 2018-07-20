package com.rygital.moneytracker.injection

import com.rygital.moneytracker.injection.modules.ActivityModule
import com.rygital.moneytracker.ui.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ActivityModule::class)])
interface ApplicationComponent {
    fun inject(activity: HomeActivity)

}