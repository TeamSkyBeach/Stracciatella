package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.item.extensions.getCreamID
import cc.lixou.stracciatella.item.extensions.setCreamID
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.Tag

open class CustomItem(
    private val id: String,
    private val material: Material,
    createMeta: (CustomItemBuilder) -> Unit = { }
) {
    companion object {
        val creamTag = Tag.String("creamID")
        val registryMap: HashMap<String, CustomItem> = HashMap()
    }

    private val eventNode = EventNode.all("item-${id}")

    @Suppress("UNCHECKED_CAST")
    private val customBuilder: CustomItemBuilder

    init {
        this.also {
            registryMap[id] = it
            customBuilder = CustomItemBuilder(it, eventNode)
        }
        createMeta.invoke(customBuilder)
        MinecraftServer.getGlobalEventHandler().addChild(eventNode)
    }

    fun createItemStack(customMaterial: Material? = null): ItemStack {
        return prepareBuilder(customMaterial).build()
    }

    fun prepareBuilder(customMaterial: Material? = null): ItemStack.Builder {
        val builder = ItemStack
            .builder(customMaterial ?: material)
        customBuilder.internalApply(builder)
        builder.meta { meta ->
            meta.setCreamID(id)
        }
        return builder
    }

    fun getCreamID(): String {
        return id
    }

    /**
     * Checks if the items all have same CreamID as this custom item
     * @return if true: all have same CreamID
     */
    fun validate(vararg others: ItemStack): Boolean {
        var result = true
        others.forEach {
            if (!it.getCreamID().equals(id)) {
                result = false
                return@forEach
            }
        }
        return result
    }

}