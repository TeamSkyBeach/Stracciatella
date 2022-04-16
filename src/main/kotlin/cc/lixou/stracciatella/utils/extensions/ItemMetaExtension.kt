package cc.lixou.stracciatella.utils.extensions

import net.minestom.server.item.ItemMeta
import net.minestom.server.tag.Tag

val creamTag = Tag.String("creamID")

fun ItemMeta.Builder.setCreamID(creamID: String): ItemMeta.Builder {
    this.set(creamTag, creamID)
    return this
}