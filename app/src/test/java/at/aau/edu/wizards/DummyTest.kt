package at.aau.edu.wizards

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DummyTest {

    @Test
    fun run() {
        val dummy = Dummy()

        dummy.increase()

        assert(dummy.counter == 1)
    }
}