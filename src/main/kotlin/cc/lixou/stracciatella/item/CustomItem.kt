package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.utils.extensions.setCreamID
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.Tag

sealed class CustomItem(
    private val id: String,
    private val material: Material,
    private val createMeta: (ItemMeta.Builder) -> Unit = { }
) {

    companion object {
        val creamTag = Tag.String("creamID")
        val registryMap: Map<String, CustomItem>
            get() = CustomItem::class.sealedSubclasses.mapNotNull { it.objectInstance }.associateBy { it.id }
    }

    fun createItemStack(): ItemStack {
        val builder = ItemStack
            .builder(material)
        builder.meta { meta ->
            createMeta.invoke(meta)
            meta.setCreamID(id)
            return@meta meta
        }
        return builder.build()
    }

    abstract fun onInteract(player: Player, action: InteractReason)

}