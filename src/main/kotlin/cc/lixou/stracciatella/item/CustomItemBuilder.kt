package cc.lixou.stracciatella.item

import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemMetaView
import net.minestom.server.item.ItemStack

class CustomItemBuilder(
    private val itemStackBuilder: ItemStack.Builder
) {
    fun meta(writeMeta: (ItemMeta.Builder) -> Unit) {
        itemStackBuilder.meta {
            writeMeta.invoke(it)
            return@meta it
        }
    }

    fun <V : ItemMetaView.Builder, T : ItemMetaView<V>> meta(metaClass: Class<T>, writeMeta: (V) -> Unit) {
        itemStackBuilder.meta(metaClass) {
            writeMeta.invoke(it)
        }
    }
}