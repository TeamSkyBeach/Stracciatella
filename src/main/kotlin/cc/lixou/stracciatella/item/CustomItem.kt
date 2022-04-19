package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.item.extensions.getCreamID
import cc.lixou.stracciatella.item.extensions.setCreamID
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent
import net.minestom.server.event.trait.PlayerEvent
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
        private val eventNode = EventNode.type("customitem-listener", EventFilter.PLAYER)

        init {
            MinecraftServer.getGlobalEventHandler().addChild(eventNode)
            eventNode.addListener(PlayerSwapItemEvent::class.java) { event ->
                event.isCancelled =
                    tryInteract(event, InteractReason.PlayerSwapItem, event.mainHandItem, event.offHandItem)
            }
            eventNode.addListener(InventoryPreClickEvent::class.java) { event ->
                event.isCancelled =
                    tryInteract(event, InteractReason.InventoryPreClickEvent, event.clickedItem, event.cursorItem)
            }
            eventNode.addListener(PlayerUseItemEvent::class.java) { event ->
                event.isCancelled = tryInteract(event, InteractReason.PlayerUseItemEvent, event.itemStack)
            }
            eventNode.addListener(PlayerUseItemOnBlockEvent::class.java) { event ->
                tryInteract(event, InteractReason.PlayerUseItemOnBlockEvent, event.itemStack)
            }
        }

        private fun tryInteract(event: PlayerEvent, reason: InteractReason, vararg tests: ItemStack): Boolean {
            return false
        }
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

    fun createItemStack(): ItemStack {
        val builder = ItemStack
            .builder(material)
        customBuilder.internalApply(builder)
        builder.meta { meta ->
            meta.setCreamID(id)
            return@meta meta
        }
        return builder.build()
    }

    fun getCreamID(): String {
        return id
    }

    /**
     * Checks if the item has same CreamID as this custom item
     * @return if true: same CreamID
     */
    fun validate(other: ItemStack): Boolean {
        return other.getCreamID().equals(id)
    }

}