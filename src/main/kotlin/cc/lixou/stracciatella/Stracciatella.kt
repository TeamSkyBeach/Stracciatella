package cc.lixou.stracciatella

import cc.lixou.stracciatella.adventure.extensions.bold
import cc.lixou.stracciatella.adventure.extensions.formatMini
import cc.lixou.stracciatella.adventure.extensions.noItalic
import cc.lixou.stracciatella.item.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.Material

class Stracciatella {

    fun start() {
        val server = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createInstanceContainer()

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }

        val testItem = CustomItem("testItem", Material.HONEYCOMB) {
            it.meta {
                it.displayName("<gradient:orange:yellow>Bienennest".formatMini().noItalic().bold())
                it.lore("lol".formatMini().color(NamedTextColor.AQUA))
            }
            it.event(PlayerSwapItemEvent::class.java, { event -> event.player.sendMessage(event.mainHandItem.toString() + " - " + event.offHandItem.toString())}, PlayerSwapItemEvent::getMainHandItem, PlayerSwapItemEvent::getOffHandItem)
        }

        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.addListener(PlayerLoginEvent::class.java) { event ->
            val player = event.player
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
            player.inventory.setItemStack(4, testItem.createItemStack())
        }

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")

        server.start("0.0.0.0", 25565)
    }

}