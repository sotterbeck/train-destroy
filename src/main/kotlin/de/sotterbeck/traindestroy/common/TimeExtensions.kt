package de.sotterbeck.traindestroy.common

import java.time.Duration
import java.time.LocalDateTime

internal val Duration.humanReadableString: String
    get() {
        val minutes = this.toMinutes().toInt()
        val seconds = this.toSecondsPart()
        return when {
            minutes == 1 -> "${minutes}m"
            minutes > 1 -> "${minutes}m and ${seconds}s"
            seconds == 1 -> "${seconds}s"
            seconds < 60 -> "${seconds}s"
            else -> "${this.toHours()}h, ${minutes}m and ${seconds}s"
        }
    }

internal val LocalDateTime.minutesSinceMidnight: Int
    get() = hour * 60 + minute
