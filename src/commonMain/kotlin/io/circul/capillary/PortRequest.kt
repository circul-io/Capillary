package io.circul.capillary

/**
 * Base interface for all port requests. Implementing classes should ideally be sealed classes
 * or sealed interfaces to represent a limited set of valid requests.
 */
interface PortRequest

suspend infix fun <REQ : PortRequest, RES : PortResponse> REQ.via(port: Port<REQ, RES>): RES =
    port.handle(this)
