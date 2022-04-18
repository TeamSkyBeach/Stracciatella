package cc.lixou.stracciatella

import cc.lixou.stracciatella.adventure.extensions.bold
import cc.lixou.stracciatella.adventure.extensions.formatMini
import cc.lixou.stracciatella.adventure.extensions.noItalic
import cc.lixou.stracciatella.item.CustomItem
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.Enchantment
import net.minestom.server.item.Material

class Stracciatella {

    fun start() {
        val server = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createInstanceContainer()

        val item = CustomItem("myCoolItem", Material.FEATHER) {
            it.meta { meta ->
                meta.displayName("<gradient:yellow:green>Test Item".formatMini().noItalic().bold())
                meta.enchantment(Enchantment.BANE_OF_ARTHROPODS, 3)
            }
            it.interaction { player, action ->
                player.openInventory(Inventory(InventoryType.CHEST_1_ROW, action.toString()))
            }
        }

        val item2 = CustomItem("ada", Material.FEATHER) {
            it.meta { meta ->
                meta.displayName("<gradient:green:blue>E Item".formatMini().noItalic().bold())
                meta.enchantment(Enchantment.BANE_OF_ARTHROPODS, 3)
            }
            it.interaction { player, action ->
                player.openInventory(Inventory(InventoryType.CHEST_1_ROW, "ada"))
            }
        }

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }

        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.addListener(PlayerLoginEvent::class.java) { event ->
            val player = event.player
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
            player.inventory.setItemStack(4, item.createItemStack())
            player.inventory.setItemStack(5, item2.createItemStack())
        }

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")

        server.start("0.0.0.0", 25565)
    }

}