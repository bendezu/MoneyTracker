package com.rygital.moneytracker.injection.components.fragment

import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.base.MyComponent
import com.rygital.moneytracker.injection.modules.FragmentModule
import com.rygital.moneytracker.injection.scopes.FragmentScope
import com.rygital.moneytracker.ui.addAccount.AddAccountFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface AddAccountFragmentComponent : MyComponent<AddAccountFragment> {

    @Subcomponent.Builder
    interface Builder: ComponentBuilder<AddAccountFragmentComponent, FragmentModule>

}