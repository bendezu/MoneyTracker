package com.rygital.moneytracker.injection

import android.content.Context
import com.rygital.moneytracker.injection.components.ApplicationComponent
import com.rygital.moneytracker.injection.components.DaggerApplicationComponent
import com.rygital.moneytracker.injection.components.activity.HomeActivityComponent
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.AddTransactionFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.DashboardFragmentComponent
import com.rygital.moneytracker.injection.components.fragment.SettingsFragmentComponent
import com.rygital.moneytracker.injection.modules.ApplicationModule
import java.io.File

class ComponentsHolder(private val context: Context) {

    private var applicationComponent: ApplicationComponent? = null

//    TODO I'll make good realisation using HashMap
//    TODO Temporary solution because I got problems with Kotlin and Dagger 2
    private var homeActivityComponent: HomeActivityComponent? = null
    private var dashboardFragmentComponent: DashboardFragmentComponent? = null
    private var settingsFragmentComponent: SettingsFragmentComponent? = null
    private var aboutFragmentComponent: AboutFragmentComponent? = null
    private var addTransactionFragmentComponent: AddTransactionFragmentComponent? = null

    fun init() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(context, File(context.cacheDir, "responses")))
                .build()
    }

    fun getHomeActivityComponent(): HomeActivityComponent? {
        if (homeActivityComponent == null)
            homeActivityComponent = applicationComponent?.createHomeActivityComponent()
        return homeActivityComponent
    }

    fun getDashboardFragmentComponent(): DashboardFragmentComponent? {
        if (dashboardFragmentComponent == null)
            dashboardFragmentComponent = applicationComponent?.createDashboardFragmentComponent()
        return dashboardFragmentComponent
    }

    fun getSettingsFragmentComponent(): SettingsFragmentComponent? {
        if (settingsFragmentComponent == null)
            settingsFragmentComponent = applicationComponent?.createSettingsFragmentComponent()
        return settingsFragmentComponent
    }

    fun getAboutFragmentComponent(): AboutFragmentComponent? {
        if (aboutFragmentComponent == null)
            aboutFragmentComponent = applicationComponent?.createAboutFragmentComponent()
        return aboutFragmentComponent
    }

    fun getAddTransactionFragmentComponent(): AddTransactionFragmentComponent? {
        if (addTransactionFragmentComponent == null)
            addTransactionFragmentComponent = applicationComponent?.createAddTransactionFragmentComponent()
        return addTransactionFragmentComponent
    }

    fun releaseHomeActivityComponent() {
        homeActivityComponent = null
    }

    fun releaseDashboardFragmentComponent() {
        dashboardFragmentComponent = null
    }

    fun releaseSettingsFragmentComponent() {
        settingsFragmentComponent = null
    }

    fun releaseAboutFragmentComponent() {
        aboutFragmentComponent = null
    }

    fun releaseAddTransactionFragmentComponent() {
        addTransactionFragmentComponent = null
    }
}