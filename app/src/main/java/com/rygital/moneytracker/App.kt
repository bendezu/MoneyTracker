package com.rygital.moneytracker

import android.app.Application
import com.rygital.moneytracker.injection.ComponentsHolder
import timber.log.Timber
import java.io.File

class App: Application() {

    var componentsHolder: ComponentsHolder? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        componentsHolder = ComponentsHolder(this, File(cacheDir, "responses"))
        componentsHolder?.init()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        var instance: App? = null
            private set
    }
}