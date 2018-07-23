package com.rygital.moneytracker

import android.app.Application
import com.rygital.moneytracker.injection.ApplicationComponent
import com.rygital.moneytracker.injection.DaggerApplicationComponent

class App: Application() {

    var applicationComponent: ApplicationComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationComponent = DaggerApplicationComponent.builder().build()
    }

    companion object {
        var instance: App? = null
            private set
    }
}