package com.rygital.moneytracker

import android.app.Application
import android.content.Context
import com.rygital.moneytracker.injection.ComponentsHolder

class App: Application() {

    var componentsHolder: ComponentsHolder? = null
        private set

    override fun onCreate() {
        super.onCreate()

        componentsHolder = ComponentsHolder(this)
        componentsHolder?.init()
    }

    companion object {
        fun getApp(context: Context): App = context.applicationContext as App
    }
}