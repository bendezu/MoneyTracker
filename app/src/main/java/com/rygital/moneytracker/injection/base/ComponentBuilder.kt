package com.rygital.moneytracker.injection.base

interface ComponentBuilder<C: MyComponent<*>, M: MyModule> {
    fun build(): C
    fun module(module: M): ComponentBuilder<C, M>
}