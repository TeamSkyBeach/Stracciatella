import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.config.YamlConfiguration

fun main(args: Array<String>) {
    val config = YamlConfiguration()
    config.load("test.yml")
    println(config.data().getValues())
//Stracciatella().start()
}