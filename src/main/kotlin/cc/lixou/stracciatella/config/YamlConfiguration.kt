package cc.lixou.stracciatella.config

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter

class YamlConfiguration : Configuration() {

    companion object {
        private val yamlOptions = DumperOptions()

        init {
            yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }

        private val yaml = Yaml(yamlOptions)
    }

    override fun load(file: File) {
        TODO("Not yet implemented")
    }

    override fun save(file: File) {
        val writer = FileWriter(file)
        val value = yaml.dump(data.getValues())
        writer.write(value)
        writer.flush()
        writer.close()
    }

}