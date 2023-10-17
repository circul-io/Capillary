package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class PortRequestTest {

    private val service = Service()

    @Test
    @JsName("testViaInfixExtensionFunction")
    fun `test via infix extension function`() {
        val result = Command.Double(24) via service
        assertEquals(result, Response.Doubled(48))
    }
}
