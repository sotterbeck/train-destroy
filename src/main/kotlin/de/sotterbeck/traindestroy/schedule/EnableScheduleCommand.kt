package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel
import de.sotterbeck.traindestroy.common.Messenger
import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

class EnableScheduleCommand(
    private val enableScheduleInteractor: EnableScheduleInteractor,
    private val messenger: Messenger,
) {
    @Command("traindestroy enable")
    @Permission("traindestroy.enable")
    suspend fun enable(sender: CommandSender) {
        when (val response = enableScheduleInteractor()) {
            is GenericResponseModel.Failure -> messenger.sendMessage(
                audience = sender,
                translationKey = response.messageKey,
                messageType = Messenger.MessageType.ERROR,
            )

            is GenericResponseModel.Success -> messenger.sendMessage(sender, response.messageKey)
        }
    }
}