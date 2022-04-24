package cc.lixou.stracciatella.game

import net.minestom.server.entity.Player
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object GameManger {

    private val games: ConcurrentHashMap<KClass<out Game>, ArrayList<out Game>> = ConcurrentHashMap()

    inline fun <reified T : Game> joinGame(players: Array<Player>) = joinGame(T::class, players)

    fun <T : Game> joinGame(clazz: KClass<out T>, players: Array<Player>) {
        val currentGames = games[clazz]
        var game: Game? = null
        currentGames?.forEach {
            if (it.canJoin(players)) {
                game = it
                return@forEach
            }
        }
        if (game == null) {
            // TODO: Create new Game
        }
        // TODO: Make Game joinable
    }

    inline fun <reified T : Game> registerGame() = registerGame(T::class)

    fun <T : Game> registerGame(clazz: KClass<out T>) {
        if (games.containsKey(clazz)) return
        games[clazz] = ArrayList()
    }

}