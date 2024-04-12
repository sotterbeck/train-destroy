package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel
import de.sotterbeck.traindestroy.common.Messenger
import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

class DisableScheduleCommand(
    private val disableScheduleInteractor: DisableScheduleInteractor,
    private val messenger: Messenger,
) {
    @Command("traindestroy disable")
    @Permission("traindestroy.disable")
    suspend fun disable(sender: CommandSender) {
        when (val response = disableScheduleInteractor()) {
            is GenericResponseModel.Failure -> messenger.sendMessage(
                audience = sender,
                translationKey = response.messageKey,
                messageType = Messenger.MessageType.ERROR,
            )

            is GenericResponseModel.Success -> messenger.sendMessage(sender, response.messageKey)
        }
    }
}