package cc.lixou.stracciatella.utils.extensions

import net.minestom.server.item.ItemStack

fun ItemStack.hasBeachID(): Boolean {
    return this.hasTag(beachTag)
}

fun ItemStack.getBeachID(): String? {
    return this.getTag(beachTag)
}