package de.sotterbeck.traindestroy.schedule

import java.time.Duration
import java.time.LocalDateTime

interface DestroyScheduleManager {

    val isScheduleActive: Boolean

    val isUpdateScheduled: Boolean

    val lastDestroy: LocalDateTime?

    val nextDestroy: LocalDateTime?

    val remainingToNextDestroy: Duration?

    fun enable()

    fun disable()

    fun updateSchedule()

    fun skipSchedule()

}