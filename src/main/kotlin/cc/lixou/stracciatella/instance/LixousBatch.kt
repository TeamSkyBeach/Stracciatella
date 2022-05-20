package cc.lixou.stracciatella.instance

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import org.jglrxavpok.hephaistos.mca.BlockPalette
import org.jglrxavpok.hephaistos.mca.BlockState
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class LixousBatch(
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int
) {

    val blocks = Array(sizeX * sizeY * sizeZ) { BlockState.AIR }
    val datas = HashMap<Vec, Pair<BlockHandler?, NBTCompound>>()
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

    fun setData(x: Number, y: Number, z: Number, data: NBTCompound, handler: BlockHandler?) {
        val vec = Vec(x.toDouble(), y.toDouble(), z.toDouble())
        if (handler != null) {
            datas[vec] = Pair(handler, data)
        } else {
            datas.remove(vec)
        }
    }

    operator fun get(x: Int, y: Int, z: Int): Block {
        val state = getState(x, y, z)
        var block = Block.fromNamespaceId(state.name)?.withProperties(state.properties)!!
        val blockData = datas[Vec(x.toDouble(), y.toDouble(), z.toDouble())]
        if (blockData != null) {
            if (blockData.first != null) {
                block = block.withHandler(blockData.first)
            }
            block = block.withNbt(blockData.second)
        }
        return block
    }

    fun getState(x: Int, y: Int, z: Int): BlockState = blocks[index(x, y, z)]

    fun index(x: Int, y: Int, z: Int): Int = x * sizeX * sizeX + y * sizeY + z

    /**
     * Pastes the batch at
     * @param pos       the location
     * @param rotation  how hard it should be rotated on the y axis. 0 = normal, 1 = 90° clockwise, 2 = 180° clockwise...
     */
    fun paste(instance: Instance, pos: Point, rotation: Byte = 0) {
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                for (z in 0 until sizeZ) {
                    val originalPos = pos.add(x.toDouble(), y.toDouble(), z.toDouble())
                    val blockPos = if (rotation == 0.toByte()) originalPos else rotate(originalPos, sizeZ)
                    instance.setBlock(blockPos, get(x, y, z))
                }
            }
        }
    }

    private fun rotate(pos: Point, size: Int) = Vec(size - 1 - pos.z(), pos.y(), pos.x())

}