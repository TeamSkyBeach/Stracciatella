package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.instance.Instance
import world.cepi.kstom.Manager
import java.util.*

abstract class Game {

    val players = ArrayList<Player>()
    lateinit var instance: Instance
        protected set
    val uuid: UUID = UUID.randomUUID()
    val eventNode =
        EventNode.type("${this.javaClass.simpleName}-${uuid}", EventFilter.INSTANCE) { event, instance ->
            if (event is PlayerEvent) {
                return@type players.contains(event.player)
            } else {
                return@type this.instance.uniqueId == instance.uniqueId
            }
        }

    init {
        Manager.globalEvent.addChild(eventNode)
    }

    /**
     * @param newPlayers the players that should join
     * @return false if game is full, then creates new instance
     */
    abstract fun canJoin(newPlayers: Array<Player>): Boolean

    abstract fun onJoin(joiningPlayer: Player)
    abstract fun onLeave(leavingPlayer: Player)

    /**
     * This gets called after a player is leaving.
     * @return if true, this game will get closed
     */
    abstract fun shouldClose(): Boolean

    abstract fun onClose()

    fun addPlayers(players: Array<Player>) {
        this.players.addAll(players)
        players.forEach { onJoin(it) }
    }

    fun removePlayer(player: Player) {
        this.players.remove(player)
        onLeave(player)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Game) {
            this.uuid == other.uuid
        } else return super.equals(other)
    }

}