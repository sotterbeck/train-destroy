package de.sotterbeck.traindestroy.config

import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.time.Duration

class ConfigLoader(
    private val configFile: FileConfiguration,
    private val plugin: Plugin
) : Reloadable {
    private lateinit var configData: TrainDestroyConfig

    val config: TrainDestroyConfig
        get() {
            if (!::configData.isInitialized) {
                load()
            }
            return configData
        }

    override fun load() {
        configData = TrainDestroyConfig(
            worlds = getWorlds(),
            interval = Duration.ofMinutes(configFile.getLong("interval")),
            countdown = getCountdown(),
            colors = getColors(),
            prefix = loadPrefix()
        )
    }

    private fun getWorlds() = configFile.getStringList("worlds")
        .mapNotNull { plugin.server.getWorld(it) }
        .toSet()

    private fun loadPrefix() = configFile.getString("messages.prefix") ?: ""

    private fun getColors() = ColorPalette(
        primary = configFile.getInt("messages.colors.primary"),
        secondary = configFile.getInt("messages.colors.secondary"),
        error = configFile.getInt("messages.colors.error")
    )

    private fun getCountdown(): Map<Int, ShowCountdownInteractor.MessageFormat> {
        val countdownSection = configFile.getConfigurationSection("countdown")
        return countdownSection?.getKeys(false)
            ?.mapNotNull { key -> key.toInt() to (countdownSection.getString(key) ?: "short") }
            ?.toMap()
            ?.mapValues { ShowCountdownInteractor.MessageFormat.valueOf(it.value.uppercase()) }
            ?: emptyMap()
    }
}