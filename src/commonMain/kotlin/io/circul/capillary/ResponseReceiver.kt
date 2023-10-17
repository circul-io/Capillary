package io.circul.capillary

import kotlin.reflect.KClass

/**
 * A receiver for a [PortResponse] that allows for pattern matching on the response type.
 *
 * @param RES The type of [PortResponse] that this receiver can handle.
 * @since 1.0.0
 */
class ResponseReceiver<RES : PortResponse>(val response: RES) {

    private var handled = false

    /**
     * Handles the response if it is of the given type.
     *
     * @param responseClass The type of response to handle.
     * @param block A function to handle the response.
     * @since 1.0.0
     */
    fun <T : RES> expects(responseClass: KClass<T>, block: ResponseReceiver<T>.() -> Unit) {
        if (responseClass.isInstance(response) && !handled) {
            @Suppress("UNCHECKED_CAST")
            block(ResponseReceiver(response as T))
            handled = true
        }
    }

    /**
     * Handles the response if it is of the given type.
     *
     * @param block A function to handle the response.
     * @since 1.0.0
     */
    inline fun <reified T : RES> expects(
        noinline block: ResponseReceiver<T>.() -> Unit
    ) = expects(T::class, block)

    /**
     * Handles the response if it has not been handled by an [expects] block.
     *
     * @param block A function to handle the response.
     * @since 1.0.0
     */
    fun otherwise(block: (ResponseReceiver<RES>) -> Unit) {
        if (!handled) {
            block(ResponseReceiver(response))
            handled = true
        }
    }
}
