package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.utils.extensions.setBeachID
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

open class CustomItem(
    private val id: String,
    private val material: Material,
    private val createMeta: (ItemMeta.Builder) -> Unit = { }
) {

    fun createItemStack(): ItemStack {
        val builder = ItemStack
            .builder(material)
        builder.meta { meta ->
            createMeta.invoke(meta)
            meta.setBeachID(id)
            return@meta meta
        }
        return builder.build()
    }

}