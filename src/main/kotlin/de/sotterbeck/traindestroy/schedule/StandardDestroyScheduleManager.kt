package de.sotterbeck.traindestroy.schedule

import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime

internal class StandardDestroyScheduleManager(
    private val intervall: Duration,
    private val clock: Clock = Clock.systemDefaultZone()
) : DestroyScheduleManager {

    private var schedule: DestroySchedule? = DestroySchedule(
        intervall = intervall,
        clock = clock
    )
    override val lastDestroy: LocalDateTime? get() = schedule?.lastDestroy

    override val isScheduleActive get() = schedule != null

    override val isUpdateScheduled get() = schedule?.isUpdateScheduled ?: false

    override val nextDestroy get() = schedule?.nextDestroy

    override val remainingToNextDestroy get() = schedule?.remainingToNextDestroy

    override fun enable() {
        schedule = DestroySchedule(intervall)
    }

    override fun disable() {
        schedule = null
    }

    override fun updateSchedule() {
        val nextSchedule = schedule?.updateSchedule()
        schedule = nextSchedule
    }

    override fun skipSchedule() {
        val next = schedule?.nextDestroy
        val skipped = next?.plus(intervall)
        schedule = schedule?.copy(nextDestroy = skipped!!)
    }
}
