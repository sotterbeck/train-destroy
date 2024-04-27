package de.sotterbeck.traindestroy

import de.sotterbeck.traindestroy.common.LocalizationSource
import de.sotterbeck.traindestroy.common.Messenger
import de.sotterbeck.traindestroy.config.ConfigLoader
import de.sotterbeck.traindestroy.config.ReloadCommand
import de.sotterbeck.traindestroy.config.Reloadable
import de.sotterbeck.traindestroy.info.StatusCommand
import de.sotterbeck.traindestroy.schedule.DestroyScheduleManager
import de.sotterbeck.traindestroy.schedule.DisableScheduleCommand
import de.sotterbeck.traindestroy.schedule.EnableScheduleCommand
import de.sotterbeck.traindestroy.schedule.TrainDestroyTask
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.util.UTF8ResourceBundleControl
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import java.util.*
import java.util.function.Consumer

private const val bundleBaseName = "messages.messages"

class TrainDestroy : JavaPlugin(), Reloadable {

    private lateinit var pluginFactory: TrainDestroyPluginFactory
    private lateinit var localizationSource: LocalizationSource

    private lateinit var scheduleManager: DestroyScheduleManager
    private lateinit var messenger: Messenger
    private lateinit var trainDestroyTask: Consumer<BukkitTask>

    override fun onEnable() {
        pluginFactory = PaperTrainDestroyPluginFactory(
            plugin = this,
            reloadable = this
        )
        localizationSource = pluginFactory.createLocalizationSource()

        saveDefaultConfig()

        buildDependencies()

        registerCommands()

        registerTranslations()

        setupTrainDestroyTask()
    }

    override fun reload() {
        reloadConfig()
        buildDependencies()
    }

    private fun buildDependencies() {
        val configLoader = ConfigLoader(config, this)
        val configData = configLoader.config

        scheduleManager = pluginFactory.createDestroyScheduleManager(intervall = configData.interval)
        messenger = pluginFactory.createMessenger(configData)

        trainDestroyTask = TrainDestroyTask(
            destroyTrains = pluginFactory.createDestroyTrainsScheduled(
                schedule = scheduleManager,
                minecartRepository = pluginFactory.createMinecartRepository(configData.worlds)
            ),
            showCountdown = pluginFactory.createShowCountdownInteractor(),
            schedule = scheduleManager,
            countdownConfig = configData.countdown,
            messenger = messenger,
        )
    }

    private fun registerCommands() {
        val commandManager = (pluginFactory as PaperTrainDestroyPluginFactory)
            .createCommandManager()

        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)
        annotationParser.installCoroutineSupport()

        annotationParser.parse(
            StatusCommand(
                statusInteractor = pluginFactory.createGetDestroyStatusInteractor(localizationSource),
                schedule = scheduleManager,
                messenger = messenger,
                plugin = this,
            ),
            EnableScheduleCommand(
                enableScheduleInteractor = pluginFactory.createEnableScheduleInteractor(scheduleManager),
                messenger = messenger
            ),
            DisableScheduleCommand(
                disableScheduleInteractor = pluginFactory.createDisableScheduleInteractor(scheduleManager),
                messenger = messenger
            ),
            ReloadCommand(
                reloadInteractor = pluginFactory.createReloadConfigInteractor(),
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
        server.scheduler.runTaskTimerAsynchronously(
            this,
            trainDestroyTask,
            delay,
            period
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
