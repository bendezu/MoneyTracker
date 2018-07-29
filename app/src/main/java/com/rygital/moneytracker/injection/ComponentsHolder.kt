package com.rygital.moneytracker.injection

import android.content.Context
import com.rygital.moneytracker.injection.base.ComponentBuilder
import com.rygital.moneytracker.injection.base.MyComponent
import com.rygital.moneytracker.injection.base.MyModule
import com.rygital.moneytracker.injection.components.ApplicationComponent
import com.rygital.moneytracker.injection.components.DaggerApplicationComponent
import com.rygital.moneytracker.injection.modules.ApplicationModule
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ComponentsHolder(private val context: Context) {

    private var applicationComponent: ApplicationComponent? = null

    @Inject @JvmSuppressWildcards
    lateinit var builders: MutableMap<KClass<*>, Provider<ComponentBuilder<MyComponent<*>, MyModule>>>

    private var components: MutableMap<KClass<*>, MyComponent<*>?> = HashMap()

    fun init() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(context)).build()
        applicationComponent?.injectComponentsHolder(this)

    }

    fun getApplicationComponent(): ApplicationComponent? {
        return applicationComponent
    }

    fun getComponent(cls: KClass<*>): MyComponent<*> {
        return getComponent(cls, null)
    }

    private fun getComponent(cls: KClass<*>, module: MyModule?): MyComponent<*> {
        var component: MyComponent<*>? = components[cls]
        if (component == null) {
            val builder = builders[cls]?.get()
            if (module != null) {
                builder.module(module)
            }
            component = builder?.build()
            components[cls] = component
        }
        return component!!
    }

    fun releaseComponent(cls: KClass<*>) {
        components.put(cls, null)
    }
}