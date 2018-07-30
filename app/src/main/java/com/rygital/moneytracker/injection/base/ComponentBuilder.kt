package com.rygital.moneytracker.injection.base

interface ComponentBuilder<out C: MyComponent<*>, in M: MyModule> {
    fun build(): C
    fun module(module: M): ComponentBuilder<C, M>
}