package com.rygital.moneytracker.injection

import com.rygital.moneytracker.injection.base.ComponentBuilder
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
internal annotation class BuilderKey(val value: KClass<out ComponentBuilder<*, *>>)