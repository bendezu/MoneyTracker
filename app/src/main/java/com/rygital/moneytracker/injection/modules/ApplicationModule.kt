package com.rygital.moneytracker.injection.modules

import android.content.Context
import com.rygital.moneytracker.data.local.DatabaseHelper
import com.rygital.moneytracker.data.local.FakeDatabaseHelperImp
import com.rygital.moneytracker.data.remote.CurrencyApi
import com.rygital.moneytracker.utils.rx.SchedulerProvider
import com.rygital.moneytracker.utils.rx.SchedulerProviderImp
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context,
                        private val cacheFile: File) {
    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi = CurrencyApi.Creator.createSlaApi(cacheFile)

    @Provides
    @Singleton
    fun provideDatabaseHelper(databaseHelper: FakeDatabaseHelperImp): DatabaseHelper = databaseHelper

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImp()
}