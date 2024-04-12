package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.Messenger
import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor
import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor.MessageFormat
import de.sotterbeck.traindestroy.removal.DestroyTrainsScheduledInteractor
import de.sotterbeck.traindestroy.removal.DestroyTrainsScheduledInteractor.ResponseModel.Idle
import de.sotterbeck.traindestroy.removal.DestroyTrainsScheduledInteractor.ResponseModel.Success
import org.bukkit.scheduler.BukkitTask
import java.util.function.Consumer

internal class TrainDestroyTask(
    private val destroyTrains: DestroyTrainsScheduledInteractor,
    private val showCountdown: ShowCountdownInteractor,
    private val schedule: DestroyScheduleManager,
    private val countdownConfig: Map<Int, MessageFormat>,
    private val messenger: Messenger,
) : Consumer<BukkitTask> {

    override fun accept(task: BukkitTask) {

        schedule.remainingToNextDestroy?.let {
            showCountdown(
                request = ShowCountdownInteractor.RequestModel(remainingTime = it, countdown = countdownConfig),
                onCountdown = { second, messageFormat ->
                    when (messageFormat) {
                        MessageFormat.SHORT -> {
                            messenger.broadcast("countdown.short", listOf(second))
                        }

                        MessageFormat.LONG -> {
                            messenger.broadcast("countdown.long", listOf(second))
                        }
                    }
                })
        }

        destroyTrains { response ->
            when (response) {
                is Success -> messenger.broadcast("trainsDestroyed", listOf(response.destroyedMinecarts.toString()))
                is Idle -> {}
            }
        }
    }

}