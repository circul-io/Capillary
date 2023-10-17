package io.circul.capillary

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class ProviderTest {

    @Test
    @JsName("testFactoryProvider")
    fun `test factory provider`() {
        val stringFactoryProvider = FactoryProvider { "Hello, World!" }
        val portFactoryProvider = FactoryProvider<Port<Command, Response>> { Service() }
        assertEquals("Hello, World!", stringFactoryProvider.provide())
        assertEquals(stringFactoryProvider.provide(), stringFactoryProvider.provide())
        assertIs<Port<Command, Response>>(portFactoryProvider.provide())
        assertNotEquals(portFactoryProvider.provide(), portFactoryProvider.provide())
    }

    @Test
    @JsName("testSingletonProvider")
    fun `test singleton provider`() {
        val stringSingletonProvider = SingletonProvider { "Hello, World!" }
        val portSingletonProvider = SingletonProvider<Port<Command, Response>> { Service() }
        assertEquals("Hello, World!", stringSingletonProvider.provide())
        assertEquals(stringSingletonProvider.provide(), stringSingletonProvider.provide())
        assertIs<Port<Command, Response>>(portSingletonProvider.provide())
        assertEquals(portSingletonProvider.provide(), portSingletonProvider.provide())
    }
}
