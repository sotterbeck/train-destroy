package de.sotterbeck.traindestroy.removal

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Minecart
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BukkitMinecartRepositoryTest {

    @MockK
    lateinit var world: World

    @Test
    fun `should return no minecarts when world has no minecarts`() {
        every { world.entities } returns listOf()
        val repository = BukkitMinecartRepository(
            worlds = setOf(world)
        )

        val minecarts = repository.getAllMinecarts()
        val minecartCount = repository.minecartCount

        assertAll {
            assertThat(minecarts).isEmpty()
            assertThat(minecartCount).isEqualTo(0)
        }
    }

    @Test
    fun `should return no minecarts when world has entities that are not minecarts`() {
        every { world.entities } returns listOf(mockkClass(Entity::class))
        val repository = BukkitMinecartRepository(
            worlds = setOf(world)
        )

        val minecarts = repository.getAllMinecarts()
        val minecartCount = repository.minecartCount

        assertAll {
            assertThat(minecarts).isEmpty()
            assertThat(minecartCount).isEqualTo(0)
        }
    }

    @Test
    fun `should return all minecarts from a world`() {
        every { world.entities } returns listOf(
            mockkClass(Entity::class),
            mockkClass(Minecart::class),
            mockkClass(Minecart::class)
        )
        val repository = BukkitMinecartRepository(
            worlds = setOf(world)
        )

        val minecarts = repository.getAllMinecarts()
        val minecartCount = repository.minecartCount

        assertAll {
            assertThat(minecarts).isNotEmpty()
            assertThat(minecartCount).isEqualTo(2)
        }
    }

    @Test
    fun `should return all minecarts from multiple worlds`() {
        val world1 = mockk<World>()
        val world2 = mockk<World>()
        every { world1.entities } returns listOf(
            mockkClass(Entity::class),
            mockkClass(Minecart::class),
            mockkClass(Minecart::class)
        )
        every { world2.entities } returns listOf(
            mockkClass(Entity::class),
            mockkClass(Entity::class),
            mockkClass(Minecart::class)
        )
        val repository = BukkitMinecartRepository(
            worlds = setOf(world1, world2),
        )

        val minecarts = repository.getAllMinecarts()
        val minecartCount = repository.minecartCount

        assertAll {
            assertThat(minecarts).isNotEmpty()
            assertThat(minecartCount).isEqualTo(3)
        }

    }

    @Test
    @Disabled("Currently disabled because minecarts are gettings removed from a different thread. Maybe MockBukkit can help.")
    fun `should remove all minecarts when there are minecarts in world`() {

        val minecart = mockk<Minecart>(relaxed = true)
        every { world.entities } returns listOf(
            mockkClass(Entity::class),
            minecart
        )
        val repository = BukkitMinecartRepository(
            worlds = setOf(world)
        )

        repository.removeAllMinecarts()

        verify { minecart.remove() }
        confirmVerified(minecart)
    }
}