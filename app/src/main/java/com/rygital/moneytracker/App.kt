package com.rygital.moneytracker

import android.app.Application
import com.rygital.moneytracker.injection.ComponentsHolder

class App: Application() {

    var componentsHolder: ComponentsHolder? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        componentsHolder = ComponentsHolder(this)
        componentsHolder?.init()
    }

    companion object {
        var instance: App? = null
            private set
    }
}