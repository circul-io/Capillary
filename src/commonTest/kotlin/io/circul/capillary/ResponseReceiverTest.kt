package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResponseReceiverTest {

    @Test
    @JsName("testExpectsMethod")
    fun `test expects method`() {
        val receiver = ResponseReceiver<Response>(Response.Doubled(20))
        var isResponse = false
        var isDoubled = false
        var isShouted = false
        var isOtherwise = false
        receiver.expects<Response> {
            isResponse = true
        }
        receiver.expects<Response.Doubled> {
            isDoubled = true
        }
        receiver.expects<Response.Shouted> {
            isShouted = true
        }
        receiver.otherwise {
            isOtherwise = true
        }
        assertTrue(isResponse)
        assertFalse(isDoubled)
        assertFalse(isShouted)
        assertFalse(isOtherwise)
    }

    @Test
    @JsName("testOtherwiseMethod")
    fun `test otherwise method`() {
        val receiver = ResponseReceiver<Response>(Response.Doubled(20))
        var isResponse = false
        var isDoubled = false
        var isShouted = false
        var isOtherwise = false
        receiver.expects<Response.Shouted> {
            isShouted = true
        }
        receiver.otherwise {
            isOtherwise = true
        }
        receiver.expects<Response> {
            isResponse = true
        }
        receiver.expects<Response.Doubled> {
            isDoubled = true
        }
        assertFalse(isResponse)
        assertFalse(isDoubled)
        assertFalse(isShouted)
        assertTrue(isOtherwise)
    }

    @Test
    @JsName("testNestedResponseReceiver")
    fun `test nested ResponseReceiver`() {
        val receiver = ResponseReceiver<Response>(Response.Doubled(20))
        var isResponse = false
        var isDoubled = false
        var isShouted = false
        var isOtherwise = false
        receiver.expects<Response> {
            isResponse = true
            expects<Response.Doubled> {
                isDoubled = true
            }
        }
        receiver.expects<Response.Shouted> {
            isShouted = true
        }
        receiver.otherwise {
            isOtherwise = true
        }
        assertTrue(isResponse)
        assertTrue(isDoubled)
        assertFalse(isShouted)
        assertFalse(isOtherwise)
    }
}
