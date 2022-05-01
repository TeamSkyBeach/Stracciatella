package cc.lixou.stracciatella.instance

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import org.jglrxavpok.hephaistos.mca.BlockPalette
import org.jglrxavpok.hephaistos.mca.BlockState

class LixousBatch(
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int
) {

    val blocks = Array(sizeX * sizeY * sizeZ) { BlockState.AIR }
    val palette = BlockPalette()

    init {
        palette.elements += BlockState.AIR
        blocks.forEach { palette.increaseReference(it) }
    }

    operator fun set(x: Int, y: Int, z: Int, block: Block) {
        val old = getState(x, y, z)
        val state = BlockState(block.name(), block.properties())
        palette.increaseReference(state)
        palette.decreaseReference(old)
        blocks[index(x, y, z)] = state
    }

    operator fun get(x: Int, y: Int, z: Int): Block? {
        val state = getState(x, y, z)
        return Block.fromNamespaceId(state.name)?.withProperties(state.properties)
    }

    fun getState(x: Int, y: Int, z: Int): BlockState = blocks[index(x, y, z)]

    fun index(x: Int, y: Int, z: Int): Int = x * sizeX * sizeX + y * sizeY + z

    fun paste(instance: Instance, pos: Point) {
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                for (z in 0 until sizeZ) {
                    val blockPos = pos.add(x.toDouble(), y.toDouble(), z.toDouble())
                    instance.setBlock(blockPos, get(x, y, z)!!)
                }
            }
        }
    }

}