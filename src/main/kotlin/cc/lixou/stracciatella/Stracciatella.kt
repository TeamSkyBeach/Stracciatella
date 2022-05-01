package cc.lixou.stracciatella

import cc.lixou.stracciatella.game.GameManager
import cc.lixou.stracciatella.instance.data.IcedSectionData
import cc.lixou.stracciatella.instance.extensions.createIcedInstance
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
import kotlin.math.floor

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
        var bool = false
        var sectest: IcedSectionData? = null
        eventHandler.addListener(PlayerChatEvent::class.java) {
            val chunk = instanceContainer.getChunkAt(it.player.position)
            val section = chunk?.getSectionAt(it.player.position.blockY())!!
            if (it.message.lowercase() == "load") {
                IcedSectionData.load(DataInputStream(FileInputStream("testlol.iced"))).paste(
                    it.player.instance!!,
                    Pos(
                        chunk.chunkX.toDouble() * 16,
                        floor(it.player.position.blockY().toDouble() / 16) * 16,
                        chunk.chunkZ.toDouble() * 16
                    )
                )
            } else if (it.message.lowercase() == "save") {
                IcedSectionData.fromChunk(
                    chunk,
                    floor(it.player.position.blockY().toDouble() / 16).toInt(), section
                ).save(DataOutputStream(FileOutputStream("testlol.iced")))
            } else if (it.message.lowercase() == "creative") {
                it.player.gameMode = GameMode.CREATIVE
            } else if (it.message.lowercase() == "spec") {
                it.player.gameMode = GameMode.SPECTATOR
            } else if (it.message.lowercase() == "savemem") {
                sectest = IcedSectionData.fromChunk(
                    chunk,
                    floor(it.player.position.blockY().toDouble() / 16).toInt(),
                    section
                )
            } else if (it.message.lowercase() == "loadmem") {
                sectest?.paste(
                    it.player.instance!!,
                    Pos(
                        chunk.chunkX.toDouble() * 16,
                        floor(it.player.position.blockY().toDouble() / 16) * 16,
                        chunk.chunkZ.toDouble() * 16
                    )
                )
            }
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