package cc.lixou.stracciatella.npc

import net.minestom.server.entity.EntityType

sealed interface NPC {

    fun createEntity(type: EntityType): EntityNPC {
        return EntityNPC(type)
    }

    fun createPlayer(): PlayerNPC {
        return PlayerNPC()
    }

}