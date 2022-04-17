package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.utils.extensions.getCustomItem
import cc.lixou.stracciatella.utils.extensions.setCreamID
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent
import net.minestom.server.event.trait.PlayerEvent
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
        private val eventNode = EventNode.type("customitem-listener", EventFilter.PLAYER)

        init {
            MinecraftServer.getGlobalEventHandler().addChild(eventNode)
            eventNode.addListener(PlayerSwapItemEvent::class.java) { event ->
                event.isCancelled = tryInteract(event, InteractReason.PlayerSwapItem, event.mainHandItem, event.offHandItem)
            }
            eventNode.addListener(InventoryPreClickEvent::class.java) { event ->
                event.isCancelled = tryInteract(event, InteractReason.InventoryPreClickEvent, event.clickedItem, event.cursorItem)
            }
            eventNode.addListener(PlayerUseItemEvent::class.java) { event ->
                event.isCancelled = tryInteract(event, InteractReason.PlayerUseItemEvent, event.itemStack)
            }
            eventNode.addListener(PlayerUseItemOnBlockEvent::class.java) { event ->
                tryInteract(event, InteractReason.PlayerUseItemOnBlockEvent, event.itemStack)
            }
        }

        private fun tryInteract(event: PlayerEvent, reason: InteractReason, vararg tests: ItemStack): Boolean {
            if(tests.isEmpty()) return false
            val player = event.player
            var cancelled = false
            for(test in tests) {
                cancelled = cancelled || (test.getCustomItem()?.onInteract(player, reason) ?: false)
            }
            return cancelled
        }
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

    fun getCreamID(): String {
        return id
    }

    /**
     * Get called when interacting with the item
     * @param player    the player which interacts
     * @param action    the action reason
     * @return boolean if the event should get cancelled
     */
    abstract fun onInteract(player: Player, action: InteractReason): Boolean

}