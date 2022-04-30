package cc.lixou.stracciatella.instance

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance
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
        val pos = getTriplePos(x, y, z)
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

    fun getState(x: Int, y: Int, z: Int): BlockState? = blocks[getTriplePos(x, y, z)]

    fun getTriplePos(x: Int, y: Int, z: Int): Triple<Byte, Byte, Byte> = Triple(y.toByte(), z.toByte(), x.toByte())
    fun getPointPos(x: Int, y: Int, z: Int): Point = Pos(y.toDouble(), z.toDouble(), x.toDouble())
    fun getPointPos(triple: Triple<Byte, Byte, Byte>): Point =
        Pos(triple.second.toDouble(), triple.third.toDouble(), triple.first.toDouble())

    fun paste(instance: Instance, pos: Point) {
        println("HÄ?!")
        for (entry in blocks) {
            val blockPos = pos.add(getPointPos(entry.key))
            val state = entry.value
            println("BLOCK $blockPos $state")
            instance.setBlock(blockPos, Block.fromNamespaceId(state.name)?.withProperties(state.properties)!!)
        }
    }

}