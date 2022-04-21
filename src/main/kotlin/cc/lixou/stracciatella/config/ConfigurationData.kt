package cc.lixou.stracciatella.config

class ConfigurationData {

    companion object {
        const val seperator = '.'
    }

    private val entries: HashMap<String, Any> = HashMap()

    fun set(path: String, value: Any?) {
        resolvePath(path).also {
            if (value == null) {
                it.first.entries.remove(it.second)
            } else {
                it.first.entries[it.second] = value
            }
        }
    }

    fun has(path: String): Boolean {
        resolvePath(path).also {
            return it.first.entries.containsKey(it.second)
        }
    }

    fun get(path: String, defaultValue: Any?): Any? {
        resolvePath(path).also {
            return it.first.entries.getOrDefault(it.second, defaultValue)
        }
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