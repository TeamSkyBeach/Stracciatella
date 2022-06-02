package cc.lixou.stracciatella.npc

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.EntityMeta
import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.minestom.server.instance.Instance
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly

class EntityNPC(
    type: EntityType,
    private val pos: Pos,
    private val instance: Instance
) {

    companion object {
        private val interactions = mutableMapOf<Int, (Player) -> Unit>()

        init {
            Manager.globalEvent.listenOnly<PlayerEntityInteractEvent> { interactions[target.entityId]?.invoke(player) }
        }
    }

    val entity: Entity = Entity(type)

    inline fun <reified T : EntityMeta> meta(callback: (T) -> Unit): EntityNPC {
        val meta = entity.entityMeta as T
        meta.setNotifyAboutChanges(false)
        callback.invoke(meta)
        meta.setNotifyAboutChanges(true)

        return this
    }

    fun spawn(): EntityNPC {
        entity.setInstance(instance, pos)
        return this
    }

    fun remove(): EntityNPC {
        entity.remove()
        return this
    }

    fun interact(interaction: (Player) -> Unit): EntityNPC {
        interactions[entity.entityId] = interaction
        return this
    }

}