package cc.lixou.stracciatella.item.impl

import cc.lixou.stracciatella.item.CustomItem
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini

object PlaceholderItem : CustomItem(
    "stracciatella:placeholder",
    Material.BEDROCK,
    {
        it.meta { meta ->
            meta.displayName("".asMini())
            meta.hideFlag(
                ItemHideFlag.HIDE_ATTRIBUTES,
                ItemHideFlag.HIDE_DYE,
                ItemHideFlag.HIDE_ENCHANTS,
                ItemHideFlag.HIDE_PLACED_ON
            )
        }

        it.event(PlayerUseItemEvent::class.java) { event ->
            if (!PlaceholderItem.validate(event.itemStack)) return@event
            event.isCancelled = true
        }
        it.event(InventoryPreClickEvent::class.java) { event ->
            if (!PlaceholderItem.validate(event.clickedItem) && !PlaceholderItem.validate(event.cursorItem)) return@event
            event.isCancelled = true
        }
        it.event(PlayerSwapItemEvent::class.java) { event ->
            if (!PlaceholderItem.validate(event.mainHandItem) && !PlaceholderItem.validate(event.offHandItem)) return@event
            event.isCancelled = true
        }
    }
) {
}