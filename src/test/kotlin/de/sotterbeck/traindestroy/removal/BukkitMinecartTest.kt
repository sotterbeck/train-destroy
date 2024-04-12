package de.sotterbeck.traindestroy.removal

import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.bukkit.entity.Minecart
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BukkitMinecartTest {

    @RelaxedMockK
    lateinit var minecartEntity: Minecart

    @Test
    fun `should execute remove function on entity`() {
        val minecart = BukkitMinecart(minecartEntity)

        minecart.remove()
        verify { minecartEntity.remove() }

        confirmVerified(minecartEntity)
    }
}