package de.sotterbeck.traindestroy.countdown

import java.time.Duration

fun interface ShowCountdownInteractor {

    operator fun invoke(request: RequestModel, onCountdown: (String, MessageFormat) -> Unit)

    data class RequestModel(
        val remainingTime: Duration,
        val countdown: Map<Int, MessageFormat> = mapOf(
            10 to MessageFormat.SHORT,
            30 to MessageFormat.LONG,
            60 to MessageFormat.LONG,
        )
    )

    enum class MessageFormat {
        LONG,
        SHORT
    }

}