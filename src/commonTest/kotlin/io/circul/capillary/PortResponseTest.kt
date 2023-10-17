package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class PortResponseTest {
    
    @Test
    @JsName("testThenInfixExtensionFunction")
    fun `test then infix extension function`() {
        Response.Doubled(20) then {
            assertEquals(response, Response.Doubled(20))
        }
    }
}
