package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.minutesSinceMidnight
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime


internal data class DestroySchedule(
    val intervall: Duration,
    val lastDestroy: LocalDateTime = LocalDateTime.now(),
    val nextDestroy: LocalDateTime = calculateNextDestroy(lastDestroy, intervall),
    private val clock: Clock = Clock.systemDefaultZone()
) {

    val remainingToNextDestroy: Duration
        get() = Duration.between(LocalDateTime.now(clock), nextDestroy)

    val isUpdateScheduled: Boolean
        get() {
            val time = LocalDateTime.now(clock)
            return time.minutesSinceMidnight % intervall.toMinutes() == 0L && time.second < 1
        }

    fun updateSchedule(): DestroySchedule {
        require(isUpdateScheduled) { "schedule can only be updated when it is scheduled" }
        println("test")
        val time = LocalDateTime.now(clock)
        return this.copy(lastDestroy = time, nextDestroy = calculateNextDestroy(time, intervall))
    }
}

private fun calculateNextDestroy(lastDestroy: LocalDateTime, intervall: Duration): LocalDateTime {
    require(intervall.toMinutes() >= 1)

    val minutesToAdd = intervall.toMinutes() - (lastDestroy.minute % intervall.toMinutes())
    return lastDestroy.plusMinutes(minutesToAdd)
        .withSecond(0)
        .withNano(0)
}
