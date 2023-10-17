package io.circul.capillary

interface Port<REQ : PortRequest, RES : PortResponse> {

    fun process(request: REQ): RES

    /**
     * Allows interactions with the port within a designated DSL scope.
     *
     * @param scope The lambda representing the specific interactions with the port.
     * @since 1.0.0
     */
    operator fun invoke(scope: Port<REQ, RES>.() -> Unit) = scope()

    /**
     * Processes the given request and handles the response using the provided [block].
     *
     * @param request The request to be processed.
     * @param block A function to handle the resulting response.
     * @since 1.0.0
     */
    fun process(request: REQ, block: (RES) -> Unit) = block(process(request))

    /**
     * Processes the given request and returns the resulting response.
     *
     * @param request The request to be processed.
     * @return The response resulting from processing the request.
     * @since 1.0.0
     */
    infix fun takes(request: REQ): RES = process(request)
}
