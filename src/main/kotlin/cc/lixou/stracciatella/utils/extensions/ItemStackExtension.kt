package cc.lixou.stracciatella.utils.extensions

import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag

fun ItemStack.Builder.setBeachID(beachID: String): ItemStack.Builder {
    this.meta { meta ->
        meta.set(Tag.String("beachID"), beachID)
        return@meta meta
    }
    return this
}