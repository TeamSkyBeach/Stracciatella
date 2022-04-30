package cc.lixou.stracciatella.instance

object TESTAAA {

    var loaded: LixousBatch? = null
    var saved: LixousBatch? = null

    fun test() {
        saved!!.blocks.forEach { (t, u) -> if (loaded!!.blocks[t] == u) return@forEach; println("WRONG: $t ${loaded!!.blocks[t]} but $u") }
    }

}