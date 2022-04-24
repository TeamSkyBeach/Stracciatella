package cc.lixou.stracciatella.item.extensions

import cc.lixou.stracciatella.item.CustomItem.Companion.creamTag
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.metadata.PlayerHeadMeta
import java.util.*

fun ItemMeta.Builder.setCreamID(creamID: String): ItemMeta.Builder {
    this.set(creamTag, creamID)
    return this
}

fun ItemMeta.Builder.setHeadTexture(texture: String): ItemMeta.Builder {
    if (this is PlayerHeadMeta.Builder) {
        skullOwner(UUID.randomUUID()).playerSkin(PlayerSkin(texture, ""))
    }
    return this
}