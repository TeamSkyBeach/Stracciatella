package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player

abstract class Game {

    protected val players = ArrayList<Player>()

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