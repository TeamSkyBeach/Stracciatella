package cc.lixou.stracciatella.instance.data

import net.minestom.server.instance.Chunk
import java.io.DataInputStream
import java.io.DataOutputStream

class IcedChunkData(
    val sectionData: Array<IcedSectionData>
) {

    companion object {

        fun load(dis: DataInputStream): IcedChunkData {
            val sections = mutableListOf<IcedSectionData>()
            for (chunkSection in 0 until 16) {
                sections.add(chunkSection, IcedSectionData.load(dis))
            }

            return IcedChunkData(sections.toTypedArray())
        }

        fun fromChunk(chunk: Chunk): IcedChunkData {
            val sections = mutableListOf<IcedSectionData>()
            for (chunkSection in 0 until 16) {
                sections.add(chunkSection, IcedSectionData.fromChunk(chunk, chunk.getSection(chunkSection)))
            }

            return IcedChunkData(sections.toTypedArray())
        }

    }

    fun save(dos: DataOutputStream) {

    }

}