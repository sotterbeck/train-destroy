package de.sotterbeck.traindestroy.common

import de.sotterbeck.traindestroy.common.Messenger.MessageType.ERROR
import de.sotterbeck.traindestroy.common.Messenger.MessageType.SUCCESS
import de.sotterbeck.traindestroy.config.TrainDestroyConfig
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.Plugin

internal class AdventureMessenger(
    private val plugin: Plugin,
    private val config: TrainDestroyConfig
) : Messenger {

    private val prefixComponent = MiniMessage.miniMessage().deserialize(config.prefix)

    override fun broadcast(translationKey: String, arguments: List<String>) {
        val component = createChatComponent(
            translationKey = translationKey,
            color = config.colors.primary,
            hasPrefix = true,
            arguments = arguments
        )
        plugin.server.sendMessage(component)
    }

    override fun sendMessage(
        audience: Audience,
        translationKey: String,
        messageType: Messenger.MessageType,
        hasPrefix: Boolean,
        arguments: List<String>
    ) {
        val component = createChatComponent(
            translationKey = translationKey,
            color = chooseColor(messageType),
            hasPrefix = hasPrefix,
            arguments = arguments
        )

        audience.sendMessage(component)
    }

    private fun createChatComponent(
        translationKey: String,
        color: Int,
        hasPrefix: Boolean = true,
        arguments: List<String>
    ): ComponentLike {
        val messageComponent = translationKeyToComponent(
            translationKey = translationKey,
            color = color,
            arguments = arguments
        )

        val component = if (hasPrefix)
            Component.join(JoinConfiguration.spaces(), prefixComponent, messageComponent)
        else
            messageComponent
        return component
    }

    private fun chooseColor(messageType: Messenger.MessageType) = when (messageType) {
        SUCCESS -> config.colors.primary
        ERROR -> config.colors.error
    }

    private fun translationKeyToComponent(
        translationKey: String,
        color: Int = config.colors.primary,
        arguments: List<String> = emptyList()
    ): ComponentLike = Component
        .translatable()
        .color(TextColor.color(color))
        .key(translationKey)
        .arguments(formatArguments(arguments))

    private fun formatArguments(arguments: List<String>): List<ComponentLike> = arguments
        .map { Component.text(it, TextColor.color(config.colors.secondary)) }
        .toList()
}