package io.circul.capillary

/**
 * A generic interface defining a Port contract in a hexagonal architecture.
 *
 * A port may be used to implement an application service (in the application/domain/business logic layer)
 * or an infrastructure/data access service (in secondary/driven/outbound adapters).
 *
 *
 *
 * @param REQ The type of requests that this port handles, must be a subtype of [PortRequest].
 * @param RES The type of responses that this port returns, must be a subtype of [PortResponse].
 */
interface Port<REQ : PortRequest, RES : PortResponse> {

    /**
     * Executes the given request and produces a response.
     *
     * @param request The request to be processed.
     * @return The response resulting from processing the request.
     */
    suspend fun handle(request: REQ): RES
}

/**
 * Processes the given request and handles the response using the provided [block].
 *
 * @param request The request to be processed.
 * @param block A function to handle the resulting response.
 */
suspend inline fun <REQ : PortRequest, RES : PortResponse> Port<REQ, RES>.handle(
    request: REQ,
    crossinline block: (RES) -> Unit
) = block(handle(request))

/**
 * Allows interactions with the port within a designated DSL scope.
 *
 * @param scope The lambda representing the specific interactions with the port.
 */
suspend inline operator fun <REQ : PortRequest, RES : PortResponse> Port<REQ, RES>.invoke(
    crossinline scope: suspend Port<REQ, RES>.() -> Unit
) = scope()

suspend infix fun <REQ : PortRequest, RES : PortResponse> Port<REQ, RES>.takes(request: REQ): RES =
    this.handle(request)

