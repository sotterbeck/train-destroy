package de.sotterbeck.traindestroy.common

import net.kyori.adventure.audience.Audience

interface Messenger {

    fun broadcast(
        translationKey: String,
        arguments: List<String> = emptyList()
    )

    fun sendMessage(
        audience: Audience,
        translationKey: String,
        messageType: MessageType = MessageType.SUCCESS,
        hasPrefix: Boolean = true,
        arguments: List<String> = emptyList()
    )

    enum class MessageType {
        SUCCESS,
        ERROR
    }
}