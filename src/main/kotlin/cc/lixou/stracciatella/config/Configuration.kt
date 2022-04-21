package cc.lixou.stracciatella.config

import java.io.File

sealed class Configuration {

    protected val data: ConfigurationData = ConfigurationData()

    abstract fun load(file: File)
    abstract fun save(file: File)

    fun data(): ConfigurationData = data

}