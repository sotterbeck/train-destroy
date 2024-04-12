package de.sotterbeck.traindestroy.removal

import org.bukkit.World

internal class BukkitMinecartRepository(
    private val worlds: Set<World>,
) : MinecartRepository {

    override fun getAllMinecarts() = worlds.flatMap { it.entities }
        .filterIsInstance<org.bukkit.entity.Minecart>()
        .map { BukkitMinecart(it) }
        .toList()

}