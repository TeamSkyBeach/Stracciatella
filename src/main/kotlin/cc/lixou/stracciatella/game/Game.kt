package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player

abstract class Game {

    /**
     * @param players the players that should join
     * @return false if game is full, then creates new instance
     */
    abstract fun canJoin(players: Array<Player>): Boolean

}