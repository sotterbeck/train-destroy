package de.sotterbeck.traindestroy

import de.sotterbeck.traindestroy.common.LocalizationSource
import de.sotterbeck.traindestroy.common.Messenger
import de.sotterbeck.traindestroy.config.ReloadConfigInteractor
import de.sotterbeck.traindestroy.config.TrainDestroyConfig
import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractor
import de.sotterbeck.traindestroy.info.GetDestroyStatusInteractor
import de.sotterbeck.traindestroy.removal.DestroyTrainsScheduledInteractor
import de.sotterbeck.traindestroy.removal.MinecartRepository
import de.sotterbeck.traindestroy.schedule.DestroyScheduleManager
import de.sotterbeck.traindestroy.schedule.DisableScheduleInteractor
import de.sotterbeck.traindestroy.schedule.EnableScheduleInteractor
import org.incendo.cloud.CommandManager
import java.time.Duration

interface TrainDestroyPluginFactory {

    fun createMessenger(config: TrainDestroyConfig): Messenger

    fun createLocalizationSource(): LocalizationSource

    fun createDestroyScheduleManager(intervall: Duration): DestroyScheduleManager

    fun createCommandManager(): CommandManager<*>

    fun createGetDestroyStatusInteractor(localizationSource: LocalizationSource): GetDestroyStatusInteractor

    fun createEnableScheduleInteractor(schedule: DestroyScheduleManager): EnableScheduleInteractor

    fun createDisableScheduleInteractor(schedule: DestroyScheduleManager): DisableScheduleInteractor

    fun createDestroyTrainsScheduled(
        schedule: DestroyScheduleManager,
        minecartRepository: MinecartRepository
    ): DestroyTrainsScheduledInteractor

    fun createShowCountdownInteractor(): ShowCountdownInteractor

    fun createReloadConfigInteractor(): ReloadConfigInteractor

    fun createMinecartRepository(worlds: Set<String>): MinecartRepository

}