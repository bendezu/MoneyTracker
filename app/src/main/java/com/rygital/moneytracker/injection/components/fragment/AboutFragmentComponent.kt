package com.rygital.moneytracker.injection.components.fragment

import com.rygital.moneytracker.injection.FragmentScope
import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.base.MyComponent
import com.rygital.moneytracker.injection.modules.FragmentModule
import com.rygital.moneytracker.ui.about.AboutFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface AboutFragmentComponent: MyComponent<AboutFragment> {

    @Subcomponent.Builder
    interface Builder: ComponentBuilder<AboutFragmentComponent, FragmentModule>
}