package com.rygital.moneytracker.injection.components

import com.rygital.moneytracker.injection.ComponentsHolder
import com.rygital.moneytracker.injection.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
    fun injectComponentsHolder(componentsHolder: ComponentsHolder)
}