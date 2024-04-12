package de.sotterbeck.traindestroy

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import de.sotterbeck.traindestroy.schedule.DestroySchedule
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.time.temporal.ChronoUnit

class DestroyScheduleTest {

    private val testBegin = Instant.parse("2024-01-01T20:00:00Z")
    private val clock: Clock = Clock.fixed(Instant.from(testBegin), ZoneId.systemDefault())

    @Test
    fun `should be invalid when intervall is under one minute`() {
        assertThrows<IllegalArgumentException> {
            DestroySchedule(
                intervall = Duration.ofSeconds(59),
                lastDestroy = LocalDateTime.now()
            )
        }
    }

    @Nested
    inner class NextDestroy {

        @Test
        fun `should calculate next destroy with intervall of one minutes`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin, ZoneId.systemDefault())

            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(1),
                lastDestroy = lastDestroy,
                clock = clock
            )

            assertThat(schedule.nextDestroy.minute).isEqualTo(1)
        }

        @Test
        fun `should calculate next destroy at intervall when last destroy not even`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin, ZoneId.systemDefault())

            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(5),
                lastDestroy = lastDestroy.plus(1, ChronoUnit.MINUTES),
                clock = clock
            )
            val nextDestroy = schedule.nextDestroy

            assertThat(nextDestroy.minute).isEqualTo(5)
        }

        @Test
        fun `should retrieve duration to next destroy`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin, ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )

            val invocation = Clock.offset(clock, Duration.ofMinutes(1))
            val duration = schedule.copy(clock = invocation).remainingToNextDestroy

            assertThat(duration).isEqualTo(Duration.ofMinutes(1))
        }
    }

    @Nested
    inner class IsUpdateScheduled {

        @Test
        fun `should not schedule update when its not divisible by intervall`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin.plus(1, ChronoUnit.MINUTES), ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )

            val invocation = Clock.offset(clock, Duration.ofMinutes(3))
            val scheduleAtInvocation = schedule.copy(clock = invocation)
            val updateScheduled = scheduleAtInvocation.isUpdateScheduled

            assertThat(updateScheduled).isFalse()
        }

        @Test
        fun `should schedule update when its divisible by intervall`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin.plus(1, ChronoUnit.MINUTES), ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )


            val invocation = Clock.offset(clock, Duration.ofMinutes(4))
            val scheduleAtInvocation = schedule.copy(clock = invocation)
            val updateScheduled = scheduleAtInvocation.isUpdateScheduled

            assertThat(updateScheduled).isTrue()
        }

        @Test
        fun `should schedule update only when its the first second of a minute`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin.plus(1, ChronoUnit.MINUTES), ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )


            val invocation = Clock.offset(
                clock, Duration.ofMinutes(4)
                    .plusSeconds(1)
            )
            val scheduleAtInvocation = schedule.copy(clock = invocation)
            val updateScheduled = scheduleAtInvocation.isUpdateScheduled

            assertThat(updateScheduled).isFalse()
        }
    }


    @Nested
    inner class UpdateSchedule {

        @Test
        fun `should not update schedule when its not scheduled`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin, ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )

            val invocation = Clock.offset(clock, Duration.ofMinutes(1))

            assertThrows<IllegalArgumentException> {
                schedule.copy(clock = invocation).updateSchedule()
            }
        }

        @Test
        fun `should update schedule when it is scheduled`() {
            val lastDestroy = LocalDateTime.ofInstant(testBegin, ZoneId.systemDefault())
            val schedule = DestroySchedule(
                intervall = Duration.ofMinutes(2),
                lastDestroy = lastDestroy
            )

            val invocation = Clock.offset(clock, Duration.ofMinutes(2))
            val updatedSchedule = schedule.copy(clock = invocation).updateSchedule()

            assertThat(updatedSchedule.lastDestroy).isNotEqualTo(lastDestroy)
        }
    }
}