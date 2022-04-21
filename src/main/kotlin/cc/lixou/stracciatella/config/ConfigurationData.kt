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

    fun setValues(values: Map<*, *>) {
        values.forEach {
            if(it.value is Map<*, *>) {
                val data = ConfigurationData()
                data.setValues(it.value as Map<*, *>)
                entries[it.key.toString()] = data
            } else if(it.value != null) {
                entries[it.key.toString()] = it.value as Any
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

    fun getValues(): Map<String, Any> {
        val values = HashMap<String, Any>()
        entries.forEach {
            var value = it.value
            if(value is ConfigurationData) {
                value = (it.value as ConfigurationData).getValues()
            }
            values[it.key] = value
        }
        return values
    }

    fun resolvePath(path: String): Pair<ConfigurationData, String> {
        var lastWord = 0
        var nextSeperator = path.indexOf(seperator, lastWord)
        var configData = this
        while (nextSeperator != -1) {
            val node: String = path.substring(lastWord, nextSeperator)
            configData = configData.entries.getOrPut(node) { return@getOrPut ConfigurationData() } as ConfigurationData

            lastWord = nextSeperator + 1
            nextSeperator = path.indexOf(seperator, lastWord)
        }
        return Pair(configData, path.substring(lastWord))
    }

}