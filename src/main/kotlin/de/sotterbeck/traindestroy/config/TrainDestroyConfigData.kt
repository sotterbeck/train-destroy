package de.sotterbeck.traindestroy.config

import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor
import java.time.Duration

data class TrainDestroyConfig(
    val worlds: Set<String>,
    val prefix: String,
    val colors: ColorPalette,
    val interval: Duration,
    val countdown: Map<Int, ShowCountdownInteractor.MessageFormat>
)

data class ColorPalette(val primary: Int, val secondary: Int, val error: Int)
