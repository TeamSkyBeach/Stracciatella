package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object GameManager {

    private val playerGame: ConcurrentHashMap<Player, Game> = ConcurrentHashMap()
    private val games: ConcurrentHashMap<KClass<out Game>, ArrayList<Game>> = ConcurrentHashMap()

    fun unregisterPlayer(player: Player) {
        leaveGame(player)
        playerGame.remove(player)
    }

    inline fun <reified T : Game> joinGame(players: Array<Player>) = joinGame(T::class, players)

    fun <T : Game> joinGame(clazz: KClass<out T>, players: Array<Player>) {
        val game: Game = games[clazz]?.find { it.canJoin(players) }
            ?: createGame(clazz)
        players.forEach {
            leaveGame(it)
            playerGame[it] = game
        }
        game.addPlayers(players)
    }

    /**
     * @return boolean if player was in a game before
     */
    fun leaveGame(player: Player): Boolean {
        val game = player.playingGame() ?: return false
        playerGame.remove(player)
        game.removePlayer(player)
        if (game.shouldClose()) {
            game.onClose()
            games[game::class]!!.remove(game)
        }
        return true
    }

    private fun <T : Game> createGame(clazz: KClass<out T>): Game {
        val game: T = clazz.primaryConstructor?.call()
            ?: throw java.lang.IllegalArgumentException("Game ${clazz.simpleName} has wrong constructor as a Game")
        games[clazz]?.add(game)
        return game
    }

    inline fun <reified T : Game> registerGame() = registerGame(T::class)

    fun <T : Game> registerGame(clazz: KClass<out T>) {
        if (games.containsKey(clazz)) return
        games[clazz] = ArrayList()
    }

    fun Player.playingGame(): Game? = getPlayingGame(this)

    fun getPlayingGame(player: Player): Game? = playerGame[player]

}
