package io.circul.capillary

import kotlin.reflect.KClass

typealias Factory<T> = () -> T

interface Provider<T> {
    fun provide(): T
}

open class FactoryProvider<T>(private val factory: Factory<T>) : Provider<T> {
    override fun provide(): T = factory()
}

open class SingletonProvider<T>(factory: Factory<T>) : Provider<T> {
    private val instance by lazy { factory() }

    override fun provide(): T = instance
}

object StringCachedProvider : SingletonProvider<String>({ "Hello World" })

val stringCachedProvider = SingletonProvider { "Hello World" }
