package cc.lixou.stracciatella.utils.extensions

import net.minestom.server.item.ItemMeta
import net.minestom.server.tag.Tag

val beachTag = Tag.String("beachID")

fun ItemMeta.Builder.setBeachID(beachID: String): ItemMeta.Builder {
    this.set(beachTag, beachID)
    return this
}