package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.instance.Instance
import world.cepi.kstom.Manager
import java.util.*

abstract class Game {

    protected val players = ArrayList<Player>()
    protected val instances = ArrayList<Instance>()
    protected val uuid: UUID = UUID.randomUUID()
    protected val eventNode =
        EventNode.type("${this.javaClass.simpleName}-${uuid}", EventFilter.INSTANCE) { event, instance ->
            if (event is PlayerEvent) {
                return@type players.contains(event.player)
            } else {
                return@type instances.firstOrNull { it.uniqueId == instance.uniqueId } != null
            }
        }

    init {
        Manager.globalEvent.addChild(eventNode)
    }

    /**
     * @param players the players that should join
     * @return false if game is full, then creates new instance
     */
    abstract fun canJoin(players: Array<Player>): Boolean

    abstract fun onJoin(player: Player)

    fun addPlayers(players: Array<Player>) {
        this.players.addAll(players)
        players.forEach { onJoin(it) }
    }

}