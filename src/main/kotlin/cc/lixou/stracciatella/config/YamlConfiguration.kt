package cc.lixou.stracciatella.config

import java.io.File

class YamlConfiguration : Configuration() {

    override fun load(file: File) {
        TODO("Not yet implemented")
    }

    override fun save(file: File) {
        println(data.getValues().toString())
        TODO("Not yet implemented")
    }

}