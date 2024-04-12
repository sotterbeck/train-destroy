package de.sotterbeck.traindestroy.info

import de.sotterbeck.traindestroy.common.Messenger
import de.sotterbeck.traindestroy.schedule.DestroyScheduleManager
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

internal class StatusCommand(
    private val statusInteractor: GetDestroyStatusInteractor,
    private val scheduleManager: DestroyScheduleManager,
    private val messenger: Messenger,
    private val plugin: Plugin
) {

    @Command("traindestroy status")
    @Permission("traindestroy.status")
    suspend fun status(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            return
        }

        val locale = sender.locale()
        val tps = plugin.server.getTps(15)

        val response = statusInteractor(
            GetDestroyStatusInteractor.RequestModel(
                locale = locale,
                remainingToNextDestroy = scheduleManager.remainingToNextDestroy,
                nextDestroy = scheduleManager.nextDestroy,
                lastDestroy = scheduleManager.lastDestroy,
                tps = tps,
                tpsThreshold = 17.0,
                isDisabled = !scheduleManager.isScheduleActive
            )
        )

        when (response) {
            is GetDestroyStatusInteractor.ResponseModel.Disabled -> {
                sendCommon(sender, response)
            }

            is GetDestroyStatusInteractor.ResponseModel.Enabled -> {
                sendCommon(sender, response)
                messenger.sendMessage(
                    audience = sender,
                    translationKey = "label.nextDestroy",
                    hasPrefix = false,
                    arguments = listOf(response.nextDestroy)
                )
                messenger.sendMessage(
                    audience = sender,
                    translationKey = "label.remainingToNextDestroy",
                    hasPrefix = false,
                    arguments = listOf(response.remainingToNextDestroy)
                )
            }

            GetDestroyStatusInteractor.ResponseModel.Error -> TODO()
        }

    }

    private fun sendCommon(
        player: Player,
        response: GetDestroyStatusInteractor.ResponseModel
    ) {
        messenger.sendMessage(
            audience = player,
            translationKey = "label.status",
            hasPrefix = false,
            arguments = listOf(response.status)
        )
        messenger.sendMessage(
            audience = player,
            translationKey = "label.performance",
            hasPrefix = false,
            arguments = listOf(response.performance)
        )
        messenger.sendMessage(
            audience = player,
            translationKey = "label.lastDestroy",
            hasPrefix = false,
            arguments = listOf(response.lastDestroy)
        )
    }

}

private fun Server.getTps(minutes: Int): Double {
    val arrayFields = mapOf(
        1 to 0,
        5 to 1,
        15 to 2
    )

    val index = arrayFields.getOrElse(minutes) { error("Timespan can only be 1, 5 or 15 minutes") }
    return tps[index]
}