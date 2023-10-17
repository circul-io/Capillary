package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PortTest {

    val service = Service()

    @Test
    @JsName("testInvokeOperator")
    fun `test invoke operator`() {
        var executedBlock = false
        service {
            executedBlock = true
            assertIs<Port<Command, Response>>(this)
            val shoutResponse = process(Command.Shout("Hello, World!"))
            assertIs<Response.Shouted>(shoutResponse)
            assertEquals("HELLO, WORLD!", shoutResponse.message)
            val doubleResponse = process(Command.Double(45))
            assertIs<Response.Doubled>(doubleResponse)
            assertEquals(90, doubleResponse.value)
        }
        assertTrue(executedBlock)
    }

    @Test
    @JsName("testProcessFunction")
    fun `test process function`() {
        var executedBlock = false
        service.process(Command.Shout("Hello, World!")) { response ->
            executedBlock = true
            assertIs<Response.Shouted>(response)
            assertEquals("HELLO, WORLD!", response.message)
        }
        assertTrue(executedBlock)
        val result = service.process(Command.Double(45))
        assertEquals(result, Response.Doubled(90))
    }

    @Test
    @JsName("testTakesInfixFunction")
    fun `test takes infix function`() {
        val response = Command.Shout("Hello, World!") via service
        assertIs<Response.Shouted>(response)
        assertEquals("HELLO, WORLD!", response.message)
    }
}
