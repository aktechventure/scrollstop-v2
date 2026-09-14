package com.aswinkumar.scrollstop.domain.engine

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PatternEngineTest {
    @Test
    fun onlyConfiguredPackagesAreMonitored() {
        val engine = PatternEngine(setOf("com.example.feed"))

        assertTrue(engine.isMonitored("com.example.feed"))
        assertFalse(engine.isMonitored("com.example.other"))
    }
}
