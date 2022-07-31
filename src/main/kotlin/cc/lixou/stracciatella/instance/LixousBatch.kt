package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.util.PasteModifier
import cc.lixou.stracciatella.instance.util.rotateClockwise
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.instance.Chunk
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
     * @param modifier  the modifier that should be applied when pasting
     */
    fun paste(instance: Instance, pos: Point, modifier: PasteModifier = PasteModifier()) {
        val modifiedChunks = mutableListOf<Chunk>()
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                for (z in 0 until sizeZ) {
                    val iterationPos = Vec(x.toDouble(), y.toDouble(), z.toDouble())
                    var blockPos = rotate(iterationPos, sizeX, sizeZ, modifier.rotationY)
                    blockPos = flip(blockPos, sizeX, sizeZ, modifier.flip)
                    val instancePos = blockPos.add(pos)
                    val chunk = instance.getChunkAt(instancePos)!!
                    if (!modifiedChunks.contains(chunk)) modifiedChunks.add(chunk)
                    val chunkPos = Vec(instancePos.x % 16, instancePos.y, instancePos.z % 16)

                    var block = get(x, y, z)
                    for (i in 0 until (modifier.rotationY % 4)) {
                        block = block.rotateClockwise()
                    }

                    chunk.setBlock(chunkPos, block) // FIXME: Light is not working correctly
                }
            }
        }
        modifiedChunks.forEach { it.sendChunk() }
    }

    private fun flip(pos: Vec, sizeX: Int, sizeZ: Int, mode: Byte): Vec {
        if (mode == 0.toByte()) return pos
        return if (mode == 1.toByte()) {
            // flip X
            pos.withX(flip(pos.x.toInt(), sizeX).toDouble() - 1.0)
        } else {
            // flip Z
            pos.withZ(flip(pos.z.toInt(), sizeZ).toDouble() - 1.0)
        }
    }

    private fun flip(point: Int, size: Int): Int {
        val half = size / 2
        return if (point > half) {
            val dif = point - half
            half - dif
        } else {
            val dif = half - point
            half + dif
        }
    }

    private fun rotate(pos: Point, sizeX: Int, sizeZ: Int, amount: Byte): Vec {
        val mod = amount % 4
        var result = Vec.fromPoint(pos)
        if (mod == 0) return result
        var remaining = mod
        while (remaining > 0) {
            result = rotateOnce(result, if (remaining % 2 == 0) sizeX else sizeZ)
            remaining--
        }
        return result
    }

    private fun rotateOnce(vec: Vec, size: Int) = Vec(size - 1 - vec.z(), vec.y(), vec.x())

}