package cc.lixou.stracciatella

import cc.lixou.stracciatella.game.GameManager
import cc.lixou.stracciatella.instance.data.IcedChunkData
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

class Stracciatella {

    fun start() {
        val server = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        val instanceContainer = instanceManager.createInstanceContainer()

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
            val chunk = instanceContainer.getChunkAt(it.player.position)
            if (it.message.lowercase() == "load") {
                IcedChunkData.load(DataInputStream(FileInputStream("mycoolchunk.iced"))).paste(
                    it.player.instance!!, chunk!!.chunkX, chunk.chunkZ
                )
            } else if (it.message.lowercase() == "save") {
                IcedChunkData.fromChunk(chunk!!).save(DataOutputStream(FileOutputStream("mycoolchunk.iced")))
            } else if (it.message.lowercase() == "creative") {
                it.player.gameMode = GameMode.CREATIVE
            } else if (it.message.lowercase() == "spec") {
                it.player.gameMode = GameMode.SPECTATOR
            }
        }

        MinecraftServer.setBrandName("Stracciatella (Minestom powered)")

        server.start("0.0.0.0", 25565)
    }

}