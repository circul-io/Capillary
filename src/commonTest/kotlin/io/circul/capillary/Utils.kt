package io.circul.capillary

sealed interface Command : PortRequest {
    data class Shout(val message: String) : Command
    data class Double(val value: Int) : Command
}

sealed interface Response : PortResponse {
    data class Shouted(val message: String) : Response
    data class Doubled(val value: Int) : Response
}

class Service : Port<Command, Response> {
    override fun process(request: Command): Response = when (request) {
        is Command.Shout -> Response.Shouted(request.message.uppercase())
        is Command.Double -> Response.Doubled(request.value * 2)
    }
}
