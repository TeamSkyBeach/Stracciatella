package cc.lixou.stracciatella.instance.data

import net.minestom.server.instance.Chunk
import java.io.DataInputStream

class IcedChunkData(
    val sectionData: Array<IcedSectionData>
) {

    companion object {

        fun load(dis: DataInputStream): IcedChunkData {
            val sections = mutableListOf<IcedSectionData>()
            for (chunkSection in 0 until 15) {
                sections.add(chunkSection, IcedSectionData.load(dis))
            }

            return IcedChunkData(sections.toTypedArray())
        }

        fun fromChunk(chunk: Chunk): IcedChunkData {
            val sections = mutableListOf<IcedSectionData>()

            return IcedChunkData(sections.toTypedArray())
        }

    }

    fun save() {

    }

}