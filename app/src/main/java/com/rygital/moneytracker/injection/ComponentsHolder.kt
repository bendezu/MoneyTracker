package com.rygital.moneytracker.injection

import android.content.Context
import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.base.MyComponent
import com.rygital.moneytracker.injection.components.ApplicationComponent
import com.rygital.moneytracker.injection.components.DaggerApplicationComponent
import com.rygital.moneytracker.injection.modules.ApplicationModule
import javax.inject.Inject
import javax.inject.Provider

class ComponentsHolder(private val context: Context) {

    private var applicationComponent: ApplicationComponent? = null

    @Inject lateinit var builders: MutableMap<Class<*>, @JvmSuppressWildcards Provider<ComponentBuilder<*, *>>>
    private var components: MutableMap<Class<*>, MyComponent<*>?> = HashMap()

    fun init() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(context)).build()
        applicationComponent?.injectComponentsHolder(this)
    }

    fun getApplicationComponent(): ApplicationComponent? {
        return applicationComponent
    }

    fun getComponent(cls: Class<*>): MyComponent<*> {
        var component: MyComponent<*>? = components[cls]
        if (component == null) {
            val builder: ComponentBuilder<*, *>? = builders[cls]?.get()
            component = builder?.build()
            components[cls] = component
        }
        return component!!
    }

    fun releaseComponent(cls: Class<*>) {
        components[cls] = null
    }
}