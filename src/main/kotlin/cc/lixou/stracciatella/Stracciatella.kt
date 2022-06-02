package cc.lixou.stracciatella

import cc.lixou.stracciatella.config.Config
import cc.lixou.stracciatella.game.GameManager
import cc.lixou.stracciatella.inventory.extensions.styleRadialBackground
import cc.lixou.stracciatella.npc.EntityNPC
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.villager.VillagerMeta
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.Material
import org.slf4j.LoggerFactory
import java.nio.file.Path

class Stracciatella {

    private val server = MinecraftServer.init()

    private val LOGGER = LoggerFactory.getLogger(Stracciatella::class.java)

    var config = ServerConfig()
        private set

    init {
        // region [Server Config]
        LOGGER.info("Loading Configuration (stracciatella.yml)")
        config = ServerConfig()
        config = Config.loadConfig(Path.of("stracciatella.yml"), config)
        // endregion


        val npc = EntityNPC(EntityType.VILLAGER, Pos(2.0, 42.0, 5.0), instanceContainer).meta<VillagerMeta> {
            it.customName = Component.text("KAKA")
            it.isCustomNameVisible = true
            val villagerData = it.villagerData
            villagerData.type = VillagerMeta.Type.DESERT
            villagerData.level = VillagerMeta.Level.MASTER
            villagerData.profession = VillagerMeta.Profession.FARMER
            it.villagerData = villagerData
        }.interact {
            val inventory = Inventory(InventoryType.CHEST_5_ROW, Component.text("Test"))
            inventory.styleRadialBackground(Material.PINK_STAINED_GLASS_PANE, 5)
            it.openInventory(inventory)
        }.spawn()

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
        LOGGER.info("Starting Stracciatella on Port ${config.port}")
        server.start("0.0.0.0", config.port)
    }

}