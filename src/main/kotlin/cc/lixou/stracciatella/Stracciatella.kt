package cc.lixou.stracciatella

import cc.lixou.stracciatella.game.GameManager
import cc.lixou.stracciatella.instance.extensions.createIcedInstance
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block

class Stracciatella {

    fun start() {
        val server = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createIcedInstance("testworld.iced")

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
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
        eventHandler.addListener(PlayerChatEvent::class.java) {
            println(instanceContainer.getChunk(0, 0)?.sections?.size)
        }

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")

        server.start("0.0.0.0", 25565)

        MinecraftServer.getSchedulerManager().buildShutdownTask {
            instanceContainer.saveChunksToStorage().run {
                instanceContainer.saveInstance().run {
                    println("Saved")
                }
            }
        }
    }

}