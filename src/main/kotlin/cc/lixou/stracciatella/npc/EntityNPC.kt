package cc.lixou.stracciatella.npc

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.EntityMeta
import net.minestom.server.instance.Instance

class EntityNPC(
    type: EntityType,
    private val pos: Pos,
    private val instance: Instance
) {

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

}