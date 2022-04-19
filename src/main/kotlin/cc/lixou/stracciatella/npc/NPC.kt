package cc.lixou.stracciatella.npc

import net.minestom.server.entity.EntityType
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.entity.fakeplayer.FakePlayerOption
import java.util.*

sealed interface NPC {

    companion object {
        fun createEntity(type: EntityType): EntityNPC {
            return EntityNPC(type)
        }

        fun createPlayer(username: String, options: FakePlayerOption = FakePlayerOption(), spawnCallback: (FakePlayer) -> Unit = { }): PlayerNPC {
            return PlayerNPC(UUID.randomUUID(), username, options, spawnCallback)
        }
    }

}