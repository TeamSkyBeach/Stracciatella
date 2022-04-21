import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.config.YamlConfiguration

fun main(args: Array<String>) {
    val config = YamlConfiguration()
    config.data().set("users.lixou.age", 13)
    config.save("test.yml")
//Stracciatella().start()
}