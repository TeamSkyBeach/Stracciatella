import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.config.Config
import cc.lixou.stracciatella.utils.serializer.PositionSerializer
import net.minestom.server.coordinate.Pos
import java.nio.file.Path

fun main(args: Array<String>) {
    val data = Test()
    Config.writeConfig(Path.of("kaka.yml"), data)
//Stracciatella().start()
}

@kotlinx.serialization.Serializable
class Test {
    @kotlinx.serialization.Serializable(PositionSerializer::class)
    val pos = Pos(40.0, 12.0, 34.0)
    @kotlinx.serialization.Serializable
    val name = "DIETER"
}