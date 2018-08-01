package com.rygital.moneytracker.injection.base

interface MyComponent<in A> {
    fun inject(activityOrFragment: A)
}