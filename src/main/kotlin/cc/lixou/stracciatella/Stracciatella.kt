package cc.lixou.stracciatella

import cc.lixou.stracciatella.npc.NPC
import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block

class Stracciatella {

    fun start() {
        val server = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createInstanceContainer()

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }

        NPC.createPlayer("Kevin") {
            it.teleport(Pos(2.0, 42.0, 5.0))
        }

        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.addListener(PlayerDisconnectEvent::class.java) { event ->
            GameManager.unregisterPlayer(event.player)
        }
        eventHandler.addListener(PlayerLoginEvent::class.java) { event ->
            val player = event.player
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
        }

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")

        server.start("0.0.0.0", 25565)
    }

}