package de.sotterbeck.traindestroy.removal

import de.sotterbeck.traindestroy.removal.DestroyTrainsScheduledInteractor.ResponseModel
import de.sotterbeck.traindestroy.schedule.DestroyScheduleManager
import org.bukkit.plugin.Plugin

internal class BukkitDestroyTrainsScheduledInteractor(
    private val minecartRepository: MinecartRepository,
    private val schedule: DestroyScheduleManager,
    private val plugin: Plugin,
) : DestroyTrainsScheduledInteractor {

    override fun invoke(destroyCallback: (ResponseModel) -> Unit) {
        if (!schedule.isScheduleActive || !schedule.isUpdateScheduled) {
            destroyCallback(ResponseModel.Idle)
            return
        }

        removeAllMinecarts(destroyCallback)

        schedule.updateSchedule()
    }

    private fun removeAllMinecarts(onDestroy: (ResponseModel) -> Unit) {
        plugin.server.scheduler.runTask(plugin) { _ ->
            val minecartCount = minecartRepository.minecartCount
            minecartRepository.removeAllMinecarts()

            onDestroy(
                ResponseModel.Success(
                    destroyedMinecarts = minecartCount
                )
            )
        }
    }

}