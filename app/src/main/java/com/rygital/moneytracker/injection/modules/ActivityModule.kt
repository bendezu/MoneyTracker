package com.rygital.moneytracker.injection.modules

import com.rygital.moneytracker.ui.home.Home
import com.rygital.moneytracker.ui.home.HomePresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule {
    @Provides
    @Singleton
    fun provideHomePresenter(presenter: HomePresenter<Home.View>): Home.Presenter<Home.View> = presenter

}