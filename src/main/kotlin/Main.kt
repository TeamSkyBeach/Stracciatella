import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.config.YamlConfiguration
import java.io.File

fun main(args: Array<String>) {
    val config = YamlConfiguration()
    config.data().set("users.lixou.age", 13)
    config.save(File("aa.yml"))
//Stracciatella().start()
}