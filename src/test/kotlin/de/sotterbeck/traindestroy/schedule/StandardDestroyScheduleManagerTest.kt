package de.sotterbeck.traindestroy.schedule

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isGreaterThan
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class StandardDestroyScheduleManagerTest {

    private val testBegin = Instant.parse("2024-01-01T20:00:00Z")
    private val clock: Clock = Clock.fixed(Instant.from(testBegin), ZoneId.systemDefault())

    @Test
    fun `should be active by default`() {
        val manager = StandardDestroyScheduleManager(Duration.ofMinutes(2))

        val isActive = manager.isScheduleActive

        assertThat(isActive).isTrue()
    }

    @Test
    fun `should disable schedule`() {
        val manager = StandardDestroyScheduleManager(Duration.ofMinutes(2))

        manager.disable()
        val isActive = manager.isScheduleActive

        assertThat(isActive).isFalse()
    }

    @Test
    fun `should re-enable schedule`() {
        val manager = StandardDestroyScheduleManager(Duration.ofMinutes(2))

        manager.disable()
        manager.enable()
        val isActive = manager.isScheduleActive

        assertThat(isActive).isTrue()
    }

    @Test
    fun `should skip next destroy`() {
        val manager = StandardDestroyScheduleManager(Duration.ofMinutes(2), clock = clock)

        manager.skipSchedule()

        assertThat(manager.remainingToNextDestroy!!.toMinutes()).isGreaterThan(2)
    }
}