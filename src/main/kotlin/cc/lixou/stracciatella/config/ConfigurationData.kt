package cc.lixou.stracciatella.config

class ConfigurationData {

    companion object {
        const val seperator = '.'
    }

    private val entries: HashMap<String, Any?> = HashMap()

    fun set(path: String, value: Any?) {

    }

    fun has(path: String): Boolean {
        return false
    }

    fun get(path: String, defaultValue: Any?): Any? {
        return null
    }

    fun resolvePath(path: String): Pair<ConfigurationData, String> {
        var lastWord = 0
        var nextSeperator = path.indexOf(seperator, lastWord)
        var configData = this
        while (nextSeperator != -1) {
            val node: String = path.substring(lastWord, nextSeperator)
            configData = entries.getOrPut(node) { return@getOrPut ConfigurationData() } as ConfigurationData

            lastWord = nextSeperator + 1
            nextSeperator = path.indexOf(seperator, lastWord)
        }
        return Pair(configData, path.substring(lastWord))
    }

}