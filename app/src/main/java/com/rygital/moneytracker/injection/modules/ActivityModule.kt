package com.rygital.moneytracker.injection.modules

import com.rygital.moneytracker.injection.ActivityScope
import com.rygital.moneytracker.injection.base.MyModule
import com.rygital.moneytracker.ui.home.Home
import com.rygital.moneytracker.ui.home.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule: MyModule {
    @Provides
    @ActivityScope
    fun provideHomePresenter(presenter: HomePresenter<Home.View>): Home.Presenter<Home.View> = presenter
}