package de.sotterbeck.traindestroy

import de.sotterbeck.traindestroy.common.AdventureMessenger
import de.sotterbeck.traindestroy.common.Messenger
import de.sotterbeck.traindestroy.common.ResourceBundleLocalizationSource
import de.sotterbeck.traindestroy.config.ConfigLoader
import de.sotterbeck.traindestroy.config.TrainDestroyConfig
import de.sotterbeck.traindestroy.countdown.ShowCountdownInteractorImpl
import de.sotterbeck.traindestroy.info.GetDestroyStatusInteractorImpl
import de.sotterbeck.traindestroy.info.StatusCommand
import de.sotterbeck.traindestroy.removal.BukkitDestroyTrainsScheduledInteractor
import de.sotterbeck.traindestroy.removal.BukkitMinecartRepository
import de.sotterbeck.traindestroy.schedule.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.util.UTF8ResourceBundleControl
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.PaperCommandManager
import java.util.*

private const val bundleBaseName = "messages.messages"

class TrainDestroy : JavaPlugin() {

    private lateinit var destroyScheduleManager: DestroyScheduleManager
    private lateinit var trainDestroyConfig: TrainDestroyConfig
    private lateinit var messenger: Messenger

    override fun onEnable() {
        saveDefaultConfig()

        val configLoader = ConfigLoader(config, this)
        trainDestroyConfig = configLoader.config
        messenger = AdventureMessenger(this, trainDestroyConfig)
        destroyScheduleManager = StandardDestroyScheduleManager(trainDestroyConfig.interval)

        registerCommands()

        registerTranslations()

        setupTrainDestroyTask()
    }

    private fun registerCommands() {
        val commandManager = PaperCommandManager(
            this,
            ExecutionCoordinator.asyncCoordinator(),
            SenderMapper.identity()
        )

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier()
        } else {
            commandManager.registerAsynchronousCompletions()
        }

        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)
        annotationParser.installCoroutineSupport()

        annotationParser.parse(
            StatusCommand(
                statusInteractor = GetDestroyStatusInteractorImpl(ResourceBundleLocalizationSource(bundleBaseName)),
                scheduleManager = destroyScheduleManager,
                messenger = messenger,
                plugin = this,
            ),
            EnableScheduleCommand(
                enableScheduleInteractor = EnableScheduleInteractorImpl(destroyScheduleManager),
                messenger = messenger
            ),
            DisableScheduleCommand(
                disableScheduleInteractor = DisableScheduleInteractorImpl(destroyScheduleManager),
                messenger = messenger
            )
        )
    }

    private fun registerTranslations() {
        val supportedLanguages = listOf(
            Locale.ENGLISH,
            Locale.GERMAN
        )
        val registry = TranslationRegistry.create(Key.key("traindestroy:translations"))

        supportedLanguages
            .map { ResourceBundle.getBundle(bundleBaseName, it, UTF8ResourceBundleControl.get()) }
            .forEach { registry.registerAll(it.locale, it, false) }

        registry.defaultLocale(Locale.ENGLISH)

        GlobalTranslator.translator().addSource(registry)
    }

    private fun setupTrainDestroyTask() {
        val delay = 0L
        val period = 20L

        val task = TrainDestroyTask(
            destroyTrains = BukkitDestroyTrainsScheduledInteractor(
                minecartRepository = BukkitMinecartRepository(
                    worlds = trainDestroyConfig.worlds
                ),
                schedule = destroyScheduleManager,
                plugin = this,
            ),
            showCountdown = ShowCountdownInteractorImpl(),
            schedule = destroyScheduleManager,
            countdownConfig = trainDestroyConfig.countdown,
            messenger = messenger,
        )

        server.scheduler.runTaskTimerAsynchronously(
            this,
            task,
            delay,
            period
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
