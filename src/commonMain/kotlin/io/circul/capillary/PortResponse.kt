package io.circul.capillary

/**
 * Base interface for all port responses. Implementing classes should ideally be sealed classes
 * or sealed interfaces to represent a limited set of valid responses.
 */
interface PortResponse

infix fun <RES : PortResponse> RES.then(block: ResponseReceiver<RES>.() -> Unit) =
    ResponseReceiver(this).block()
