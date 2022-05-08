package cc.lixou.stracciatella

import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block

class Stracciatella {

    private val server = MinecraftServer.init()

    init {
        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.addListener(PlayerDisconnectEvent::class.java) { event ->
            GameManager.unregisterPlayer(event.player)
        }

        // region [Test World Debug Stuff - REMOVE]
        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createInstanceContainer()

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }
        eventHandler.addListener(PlayerLoginEvent::class.java) { event ->
            val player = event.player
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
        }
        // endregion

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")
    }

    fun start() {
        server.start("0.0.0.0", 25565)
    }

}