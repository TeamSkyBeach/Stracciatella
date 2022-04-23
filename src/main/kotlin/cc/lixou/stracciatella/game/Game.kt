package cc.lixou.stracciatella.game

abstract class Game {

    /**
     * @return false if game is full, then creates new instance
     */
    abstract fun canJoin(): Boolean

}