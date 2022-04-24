package cc.lixou.stracciatella.game

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object GameManger {

    private val games: ConcurrentHashMap<KClass<out Game>, ArrayList<out Game>> = ConcurrentHashMap()

    inline fun <reified T : Game> registerGame() = registerGame(T::class)

    fun <T : Game> registerGame(clazz: KClass<out T>) {
        if (games.containsKey(clazz)) return
        games[clazz] = ArrayList()
    }

}