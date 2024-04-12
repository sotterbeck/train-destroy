package de.sotterbeck.traindestroy.removal

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import de.sotterbeck.traindestroy.schedule.DestroyScheduleManager
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DestroyTrainsScheduledInteractorImplTest {

    @MockK
    lateinit var schedule: DestroyScheduleManager

    @RelaxedMockK
    lateinit var minecartRepository: MinecartRepository

    private lateinit var server: ServerMock
    private lateinit var plugin: Plugin

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.createMockPlugin()

        every { schedule.updateSchedule() } answers {}
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `should not update schedule when schedule is disabled`() {
        every { schedule.isScheduleActive } returns false
        every { schedule.isUpdateScheduled } returns false

        val destroyTrains = BukkitDestroyTrainsScheduledInteractor(
            minecartRepository = minecartRepository,
            schedule = schedule,
            plugin = plugin,
        )

        destroyTrains {}
        verify(exactly = 0) { schedule.updateSchedule() }
    }

    @Test
    fun `should not update schedule when it is not scheduled`() {
        every { schedule.isScheduleActive } returns true
        every { schedule.isUpdateScheduled } returns false

        val destroyTrains = BukkitDestroyTrainsScheduledInteractor(
            minecartRepository = minecartRepository,
            schedule = schedule,
            plugin = plugin
        )

        destroyTrains {}
        verify(exactly = 0) { schedule.updateSchedule() }
    }

    @Test
    fun `should update schedule when its active and scheduled`() {
        every { schedule.isScheduleActive } returns true
        every { schedule.isUpdateScheduled } returns true

        val destroyTrains = BukkitDestroyTrainsScheduledInteractor(
            minecartRepository = minecartRepository,
            schedule = schedule,
            plugin = plugin
        )

        destroyTrains {}
        verify { schedule.updateSchedule() }
    }
}