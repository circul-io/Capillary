package io.circul.capillary

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals


sealed interface CommandRequest : PortRequest {
    data class CapitalizeIt(val s: String) : CommandRequest
    data class DoubleIt(val i: Int) : CommandRequest
    data object AcknowledgeMe : CommandRequest
}

sealed interface CommandResponse : PortResponse {
    data class Capitalized(val s: String) : CommandResponse
    data class Doubled(val i: Int) : CommandResponse
    sealed interface Acknowledgement : CommandResponse
    data object OK : CommandResponse, Acknowledgement
    data object Hello : CommandResponse, Acknowledgement
}

class CommandService : Port<CommandRequest, CommandResponse> {
    override suspend fun handle(request: CommandRequest): CommandResponse = when (request) {
        is CommandRequest.CapitalizeIt -> capitalize(request)
        is CommandRequest.DoubleIt -> double(request)
        CommandRequest.AcknowledgeMe -> acknowledge()
    }

    private fun capitalize(command: CommandRequest.CapitalizeIt) =
        CommandResponse.Capitalized(command.s.uppercase())

    private fun double(command: CommandRequest.DoubleIt) = CommandResponse.Doubled(command.i * 2)

    private fun acknowledge(): CommandResponse.Acknowledgement = CommandResponse.OK
}

class PortTest {

    private val service: Port<CommandRequest, CommandResponse>

    init {
        service = CommandService()
    }

    @Test
    fun testHandleMethod() = runTest {
        assertIs<CommandResponse.OK>(service.handle(CommandRequest.AcknowledgeMe))
        assertIs<CommandResponse.Acknowledgement>(service.handle(CommandRequest.AcknowledgeMe))
        assertIs<CommandResponse.Doubled>(service.handle(CommandRequest.DoubleIt(7)))
        assertIs<CommandResponse.Capitalized>(service.handle(CommandRequest.CapitalizeIt("Hello, World!")))
    }

    @Test
    fun testHandleInlineFunction() = runTest {
        var response: CommandResponse? = null
        service.handle(CommandRequest.AcknowledgeMe) {
            response = it
        }
        assertIs<CommandResponse.Acknowledgement>(response)
        assertIs<CommandResponse.OK>(response)
    }

    @Test
    fun testPortInvokeOperator() = runTest {
        var response: CommandResponse? = null
        service {
            response = this.handle(CommandRequest.AcknowledgeMe)
        }
        assertIs<CommandResponse.Acknowledgement>(response)
        assertIs<CommandResponse.OK>(response)
    }

    @Test
    fun testRequestViaPortInfixFunction() = runTest {
        val response = CommandRequest.AcknowledgeMe via service
        assertIs<CommandResponse.Acknowledgement>(response)
        assertIs<CommandResponse.OK>(response)
    }

    @Test
    fun testPortTakesRequestInfixFunction() = runTest {
        val response = service takes CommandRequest.AcknowledgeMe
        assertIs<CommandResponse.Acknowledgement>(response)
        assertIs<CommandResponse.OK>(response)
    }


    @Test
    fun testRequestThenResponseReceiverLambda() = runTest {
        var response: CommandResponse? = null
        CommandRequest.DoubleIt(8) via service then {
            response = this.response
        }
        assertIs<CommandResponse.Doubled>(response)
    }


    @Test
    fun testResponseReceiverExpectMethod() = runTest {
        var expectEvaluationCounter = 0
        CommandRequest.AcknowledgeMe via service then {
            expects<CommandResponse.Acknowledgement> { ack ->
                expectEvaluationCounter++ // 1
                ack.expects<CommandResponse.OK> {
                    expectEvaluationCounter++ // 2
                }
                ack.expects<CommandResponse.Hello> {
                    throw RuntimeException("This block should not be executed")
                }
            }
            expects<CommandResponse.OK> {
                expectEvaluationCounter++ // 3
            }
            expects<CommandResponse.Doubled> {
                throw RuntimeException("This block should not be executed")
            }
        }
        assertEquals(expectEvaluationCounter, 3)
    }
}
