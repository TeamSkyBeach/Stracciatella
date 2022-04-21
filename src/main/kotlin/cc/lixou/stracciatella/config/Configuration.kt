package cc.lixou.stracciatella.config

import java.io.File

sealed class Configuration {

    protected val data: ConfigurationData = ConfigurationData()

    fun load(fileName: String) {
        load(File(fileName))
    }
    abstract fun load(file: File)
    fun save(fileName: String) {
        save(File(fileName))
    }
    abstract fun save(file: File)

    fun data(): ConfigurationData = data

}