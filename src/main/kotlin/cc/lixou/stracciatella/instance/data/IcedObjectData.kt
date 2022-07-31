package cc.lixou.stracciatella.instance.data

import cc.lixou.stracciatella.instance.util.NBTUtils
import cc.lixou.stracciatella.instance.util.PasteModifier
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import java.io.DataInputStream
import java.io.DataOutputStream

class IcedObjectData(
    private val sizeX: UShort,
    private val sizeZ: UShort,
    private val chunkData: Map<Pair<Short, Short>, IcedChunkData>,
    val additionalData: NBTCompound = NBT.Compound()
) {

    companion object {

        fun load(dis: DataInputStream): IcedObjectData {
            val sizeX = dis.readUnsignedShort().toUShort()
            val sizeZ = dis.readUnsignedShort().toUShort()

            val chunkData = mutableMapOf<Pair<Short, Short>, IcedChunkData>()
            for (x in 0 until sizeX.toInt()) {
                for (z in 0 until sizeZ.toInt()) {
                    chunkData[Pair(x.toShort(), z.toShort())] = IcedChunkData.load(dis)
                }
            }

            val additionalDataSize = dis.readInt()
            val additionalData = if (additionalDataSize == 0) NBTCompound() else {
                val additionalDataBytes = dis.readNBytes(additionalDataSize)
                NBTUtils.readNBTTag<NBTCompound>(additionalDataBytes)
            }

            return IcedObjectData(sizeX, sizeZ, chunkData, additionalData)
        }

        fun fromChunks(chunks: List<Chunk>): IcedObjectData {
            val minX = chunks.minOf { it.chunkX }
            val minZ = chunks.minOf { it.chunkZ }
            val sizeX = chunks.maxOf { it.chunkX } - minX + 1
            val sizeZ = chunks.maxOf { it.chunkZ } - minZ + 1

            val chunkData = mutableMapOf<Pair<Short, Short>, IcedChunkData>()
            for (chunk in chunks) {
                chunkData[Pair((chunk.chunkX - minX).toShort(), (chunk.chunkZ - minZ).toShort())] =
                    IcedChunkData.fromChunk(chunk)
            }

            return IcedObjectData(sizeX.toUShort(), sizeZ.toUShort(), chunkData)
        }
    }

    fun save(dos: DataOutputStream) {
        dos.writeShort(sizeX.toInt())
        dos.writeShort(sizeZ.toInt())

        println(chunkData)
        for (x in 0 until sizeX.toInt()) {
            for (z in 0 until sizeZ.toInt()) {
                println("SAVING $x $z")
                chunkData[Pair(x.toShort(), z.toShort())]?.save(dos)
            }
        }

        if (additionalData.isEmpty()) {
            dos.writeInt(0)
        } else {
            val additionalDataBytes = NBTUtils.writeNBTTag(additionalData)
            dos.writeInt(additionalDataBytes.size)
            dos.write(additionalDataBytes)
        }
    }

    fun paste(instance: Instance, chunkX: Int, chunkZ: Int, modifier: PasteModifier = PasteModifier()) {
        for (x in 0 until sizeX.toInt()) {
            for (z in 0 until sizeZ.toInt()) {
                var pos = rotate(x, z, sizeX, sizeZ, modifier.rotationY)
                pos = flip(pos, sizeX, sizeZ, modifier.flip)
                chunkData[Pair(x.toShort(), z.toShort())]?.paste(
                    instance,
                    pos.first + chunkX,
                    pos.second + chunkZ,
                    modifier
                )
            }
        }
    }

    private fun flip(pos: Pair<Short, Short>, sizeX: UShort, sizeZ: UShort, mode: Byte): Pair<Short, Short> {
        if (mode == 0.toByte()) return pos
        return if (mode == 1.toByte()) {
            // flip X
            Pair((flip(pos.first.toInt(), sizeX.toInt()) - 1).toShort(), pos.second)
        } else {
            // flip Z
            Pair(pos.first, (flip(pos.second.toInt(), sizeZ.toInt()) - 1).toShort())
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

    private fun rotate(x: Int, z: Int, sizeX: UShort, sizeZ: UShort, amount: Byte): Pair<Short, Short> {
        val mod = amount % 4
        var result = Pair(x.toShort(), z.toShort())
        if (mod == 0) return result
        var remaining = mod
        while (remaining > 0) {
            result = rotateOnce(result, if (remaining % 2 == 0) sizeX.toInt() else sizeZ.toInt())
            remaining--
        }
        return result
    }

    private fun rotateOnce(pos: Pair<Short, Short>, size: Int) = Pair((size - 1 - pos.second).toShort(), pos.first)

}