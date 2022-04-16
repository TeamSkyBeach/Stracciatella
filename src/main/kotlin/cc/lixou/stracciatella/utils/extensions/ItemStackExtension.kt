package cc.lixou.stracciatella.utils.extensions

import cc.lixou.stracciatella.item.CustomItem.Companion.creamTag
import net.minestom.server.item.ItemStack

fun ItemStack.hasCreamID(): Boolean {
    return this.hasTag(creamTag)
}

fun ItemStack.getCreamID(): String? {
    return this.getTag(creamTag)
}