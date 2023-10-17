package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentTest {

    private val serviceProvider = SingletonProvider<Port<Command, Response>> { Service() }

    @Test
    @JsName("testEverythingTogether")
    fun `test everything together`() {
        val service = serviceProvider.provide()
        var resultString: String? = null
        var resultInt: Int? = null

        Command.Shout("Hello, World!") via service then {
            expects<Response.Shouted> {
                resultString = response.message
            }
            expects<Response.Doubled> {
                throw AssertionError("Expected Response.Shouted but got $response")
            }
            otherwise {
                throw AssertionError("Expected Response.Shouted but got $response")
            }
        }
        assertEquals(resultString, "HELLO, WORLD!")

        service takes Command.Double(150) then {
            expects<Response.Shouted> {
                throw AssertionError("Expected Response.Shouted but got $response")
            }
            expects<Response.Doubled> {
                resultInt = response.value
            }
            otherwise {
                throw AssertionError("Expected Response.Shouted but got $response")
            }
        }
        assertEquals(resultInt, 300)

        service {
            process(Command.Shout("Goodbye, World!")) {
                when (it) {
                    is Response.Doubled -> throw AssertionError("Expected Response.Shouted but got $it")
                    is Response.Shouted -> resultString = it.message
                }
            }
            when (val response = process(Command.Double(300))) {
                is Response.Doubled -> resultInt = response.value
                is Response.Shouted -> throw AssertionError("Expected Response.Shouted but got $response")
            }
        }
        assertEquals(resultString, "GOODBYE, WORLD!")
        assertEquals(resultInt, 600)
    }
}
