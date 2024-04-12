package de.sotterbeck.traindestroy.removal

internal class BukkitMinecart(
    private val minecartEntity: org.bukkit.entity.Minecart
) : Minecart {

    override fun remove() {
        minecartEntity.remove()
    }
}