package io.circul.capillary

/**
 * Base interface for all port requests. Implementing classes should ideally be sealed classes
 * or sealed interfaces to represent a limited set of valid requests.
 *
 * @since 1.0.0
 */
interface PortRequest

/**
 * Infix function to chain a request to a port, processing it and returning a response.
 *
 * @param REQ The type of requests that this port handles, must be a subtype of [PortRequest].
 * @param RES The type of responses that this port returns, must be a subtype of [PortResponse].
 * @param port The port to process the request.
 * @since 1.0.0
 */
infix fun <REQ : PortRequest, RES : PortResponse> REQ.via(port: Port<REQ, RES>): RES =
    port.process(this)
