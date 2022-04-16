package cc.lixou.stracciatella.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.item.Enchantment
import net.minestom.server.item.Material

object MyCoolItem : CustomItem(
    "myCoolItem",
    Material.FEATHER,
    {
        it.displayName(Component.text("MyCoolFeather", NamedTextColor.DARK_GRAY))
        it.enchantment(Enchantment.BANE_OF_ARTHROPODS, 2)
    }
) {
    override fun onInteract(player: Player, action: InteractReason) {

    }
}