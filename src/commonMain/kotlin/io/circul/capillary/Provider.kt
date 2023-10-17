package io.circul.capillary

/**
 * A factory function that creates an instance of type [T].
 *
 * @since 1.0.0
 */
typealias Factory<T> = () -> T

/**
 * A provider of instances of type [T].
 *
 * @since 1.0.0
 */
interface Provider<T> {
    fun provide(): T
}

/**
 * A provider of instances of type [T] that are created by the given [factory].
 *
 * @param factory The factory function that creates instances of type [T].
 * @since 1.0.0
 */
open class FactoryProvider<T>(private val factory: Factory<T>) : Provider<T> {
    override fun provide(): T = factory()
}

/**
 * A provider of instances of type [T] that are created by the given [Factory] and cached.
 *
 * @param factory The factory function that creates instances of type [T].
 * @since 1.0.0
 */
open class SingletonProvider<T>(factory: Factory<T>) : Provider<T> {
    private val instance by lazy { factory() }

    override fun provide(): T = instance
}
