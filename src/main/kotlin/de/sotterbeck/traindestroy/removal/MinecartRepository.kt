package de.sotterbeck.traindestroy.removal

interface MinecartRepository {

    fun getAllMinecarts(): List<Minecart>

    fun removeAllMinecarts() = getAllMinecarts().forEach { it.remove() }

    val minecartCount get() = getAllMinecarts().count()
}