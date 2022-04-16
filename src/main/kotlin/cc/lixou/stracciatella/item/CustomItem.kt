package cc.lixou.stracciatella.item

import net.kyori.adventure.text.Component
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

open class CustomItem(
    private val id: String,
    private val material: Material,
    private val displayName: Component? = null,
    private val createMeta: (ItemMeta.Builder) -> Unit = { }
) {

    fun createItemStack(): ItemStack {
        val builder = ItemStack
            .builder(material)
        if(displayName != null) {
            builder.displayName(displayName)
        }
        builder.meta { meta ->
            createMeta.invoke(meta)
            return@meta meta
        }
        return builder.build()
    }

}