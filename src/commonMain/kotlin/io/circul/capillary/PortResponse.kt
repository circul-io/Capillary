package io.circul.capillary

/**
 * Base interface for all port responses. Implementing classes should ideally be sealed classes
 * or sealed interfaces to represent a limited set of valid responses.
 *
 * @since 1.0.0
 */
interface PortResponse

/**
 * Infix function to chain a response receiver to a response.
 *
 * @see ResponseReceiver
 * @since 1.0.0
 */
infix fun <RES : PortResponse> RES.then(block: ResponseReceiver<RES>.() -> Unit) =
    ResponseReceiver(this).block()
