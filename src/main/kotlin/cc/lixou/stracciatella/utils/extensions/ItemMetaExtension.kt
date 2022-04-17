package cc.lixou.stracciatella.utils.extensions

import cc.lixou.stracciatella.item.CustomItem.Companion.creamTag
import net.minestom.server.item.ItemMeta

fun ItemMeta.Builder.setCreamID(creamID: String): ItemMeta.Builder {
    this.set(creamTag, creamID)
    return this
}