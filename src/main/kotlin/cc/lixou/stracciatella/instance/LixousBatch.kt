package cc.lixou.stracciatella.instance

import net.minestom.server.instance.block.Block
import org.jglrxavpok.hephaistos.mca.BlockPalette
import org.jglrxavpok.hephaistos.mca.BlockState

class LixousBatch {

    val blocks = mutableMapOf<Triple<Byte, Byte, Byte>, BlockState>()
    val palette = BlockPalette()

    init {
        palette.elements += BlockState.AIR
    }

    operator fun set(x: Int, y: Int, z: Int, block: Block) {
        val pos = Triple(x.toByte(), y.toByte(), z.toByte())
        val old = blocks[pos]
        val state = BlockState(block.name(), block.properties())
        palette.increaseReference(state)
        if (old != null) {
            palette.decreaseReference(old)
        }
        blocks[pos] = state
    }

    operator fun get(x: Int, y: Int, z: Int): Block? {
        val state = getState(x, y, z) ?: return null
        return Block.fromNamespaceId(state.name)?.withProperties(state.properties)
    }

    fun getState(x: Int, y: Int, z: Int): BlockState? = blocks[Triple(x.toByte(), y.toByte(), z.toByte())]

}