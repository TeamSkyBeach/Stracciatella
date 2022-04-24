package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object GameManger {

    private val games: ConcurrentHashMap<KClass<out Game>, ArrayList<Game>> = ConcurrentHashMap()

    inline fun <reified T : Game> joinGame(players: Array<Player>) = joinGame(T::class, players)

    fun <T : Game> joinGame(clazz: KClass<out T>, players: Array<Player>) {
        val game: Game = games[clazz]?.find { it.canJoin(players) }
            ?: createGame(clazz)
        game.addPlayers(players)
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

}