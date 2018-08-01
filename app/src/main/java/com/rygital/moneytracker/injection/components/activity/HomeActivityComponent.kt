package com.rygital.moneytracker.injection.components.activity

import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.base.MyComponent
import com.rygital.moneytracker.injection.modules.ActivityModule
import com.rygital.moneytracker.injection.scopes.ActivityScope
import com.rygital.moneytracker.ui.home.HomeActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface HomeActivityComponent: MyComponent<HomeActivity> {

    @Subcomponent.Builder
    interface Builder: ComponentBuilder<HomeActivityComponent, ActivityModule>
}