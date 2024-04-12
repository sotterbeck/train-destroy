package de.sotterbeck.traindestroy.config

import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor
import org.bukkit.World
import java.time.Duration

data class TrainDestroyConfig(
    val worlds: Set<World>,
    val prefix: String,
    val colors: ColorPalette,
    val interval: Duration,
    val countdown: Map<Int, ShowCountdownInteractor.MessageFormat>
)

data class ColorPalette(val primary: Int, val secondary: Int, val error: Int)
