package com.aswinkumar.scrollstop

import org.junit.Test

import org.junit.Assert.*
import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.domain.model.PermissionStatus

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun permissionState_distinguishes_required_denied_and_granted() {
        assertEquals(
            PermissionStatus.REQUIRED,
            PermissionStatus.resolve(granted = false, wasPreviouslyGranted = false)
        )
        assertEquals(
            PermissionStatus.DENIED,
            PermissionStatus.resolve(granted = false, wasPreviouslyGranted = true)
        )
        assertEquals(
            PermissionStatus.GRANTED,
            PermissionStatus.resolve(granted = true, wasPreviouslyGranted = true)
        )
    }
}