package cc.lixou.stracciatella.instance.data

import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
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
                sections.add(
                    chunkSection,
                    IcedSectionData.fromChunk(chunk, chunkSection, chunk.getSection(chunkSection))
                )
            }

            return IcedChunkData(sections.toTypedArray())
        }

    }

    fun save(dos: DataOutputStream) {
        for (dataSection in sectionData) {
            dataSection.save(dos)
        }
    }

    fun paste(instance: Instance, chunkX: Int, chunkZ: Int) {
        sectionData.forEachIndexed { index, icedSectionData ->
            icedSectionData.paste(
                instance,
                Pos(chunkX.toDouble() * 16, index.toDouble() * 16, chunkZ.toDouble() * 16),
                1
            )
        }
    }

}