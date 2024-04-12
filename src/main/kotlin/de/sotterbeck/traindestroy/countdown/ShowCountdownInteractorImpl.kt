package de.sotterbeck.traindestroy.countdown

import de.sotterbeck.traindestroy.common.humanReadableString
import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor.MessageFormat

internal class ShowCountdownInteractorImpl : ShowCountdownInteractor {

    override fun invoke(
        request: ShowCountdownInteractor.RequestModel,
        onCountdown: (String, MessageFormat) -> Unit
    ) {
        val remainingSeconds = request.remainingTime.seconds.toInt()

        if (!request.countdown.keys.contains(remainingSeconds)) {
            return
        }

        onCountdown(
            request.remainingTime.humanReadableString,
            request.countdown.getOrDefault(remainingSeconds, MessageFormat.SHORT)
        )
    }

}

