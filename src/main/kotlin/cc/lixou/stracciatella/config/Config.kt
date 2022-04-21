package cc.lixou.stracciatella.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object Config {

    inline fun <reified T : Any> loadConfig(path: Path, emptyObj: T): T {
        return if(path.exists()) Yaml.default.decodeFromString(path.readText()) else run {
            path.writeText(Yaml.default.encodeToString(emptyObj))
            emptyObj
        }
    }

    inline fun <reified T : Any> writeConfig(path: Path, data: T) {
        path.writeText(Yaml.default.encodeToString(data))
    }

}