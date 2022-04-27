package cc.lixou.stracciatella.instance.data

import net.minestom.server.instance.Chunk
import java.io.DataInputStream
import java.io.DataOutputStream

class IcedObjectData(
    val sizeX: UShort,
    val sizeZ: UShort,
    val chunkData: Map<Pair<Short, Short>, IcedChunkData>
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

            return IcedObjectData(sizeX, sizeZ, chunkData)
        }

        fun fromChunks(chunks: List<Chunk>): IcedObjectData {
            val sizeX = chunks.maxOf { it.chunkX } - chunks.minOf { it.chunkX } + 1
            val sizeZ = chunks.maxOf { it.chunkZ } - chunks.minOf { it.chunkZ } + 1

            val chunkData = mutableMapOf<Pair<Short, Short>, IcedChunkData>()
            for (chunk in chunks) {
                chunkData[Pair(chunk.chunkX.toShort(), chunk.chunkZ.toShort())] = IcedChunkData.fromChunk(chunk)
            }

            return IcedObjectData(sizeX.toUShort(), sizeZ.toUShort(), chunkData)
        }
    }

    fun save(dos: DataOutputStream) {
        dos.writeShort(sizeX.toInt())
        dos.writeShort(sizeZ.toInt())

        for (x in 0 until sizeX.toInt()) {
            for (z in 0 until sizeZ.toInt()) {
                chunkData[Pair(x.toShort(), z.toShort())]?.save(dos)
            }
        }
    }

}