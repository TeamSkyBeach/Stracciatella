package cc.lixou.stracciatella.instance.data

import cc.lixou.stracciatella.instance.util.NBTUtils
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Section
import net.minestom.server.instance.batch.RelativeBlockBatch
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mca.unpack
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import java.io.DataInputStream
import java.io.DataOutputStream

class IcedSectionData(
    val batch: RelativeBlockBatch,
    val skyLight: ByteArray,
    val blockLight: ByteArray
) {

    companion object {

        /** @author inspired by CatDevz/SlimeLoader on GitHub */
        fun load(dis: DataInputStream): IcedSectionData {
            // Light Data
            val hasBlockLight = dis.readBoolean()
            val blockLight = if (hasBlockLight) {
                dis.readNBytes(2048)
            } else ByteArray(2048)

            // Palette Data
            val paletteLength = dis.readInt()
            val paletteList = mutableListOf<NBTCompound>()

            for (i in 0 until paletteLength) {
                val nbtLength = dis.readInt()
                val nbtRaw = dis.readNBytes(nbtLength)
                val nbtCompound = NBTUtils.readNBTTag<NBTCompound>(nbtRaw) ?: continue
                paletteList.add(nbtCompound)
            }

            // Block States
            val blockStatesLength = dis.readInt()
            val compactedBlockStates = ImmutableLongArray(blockStatesLength) { dis.readLong() }

            val sizeInBits = compactedBlockStates.size * 64 / 4096
            val blockStates = unpack(compactedBlockStates, sizeInBits).sliceArray(0 until 4096)

            val batch = RelativeBlockBatch()

            for (y in 0 until 16) {
                for (z in 0 until 16) {
                    for (x in 0 until 16) {
                        val pos = y * 16 * 16 + z * 16 + x
                        val value = paletteList[blockStates[pos]]
                        val block = NBTUtils.getBlockFromCompound(value) ?: continue
                        batch.setBlock(x, y, z, block)
                    }
                }
            }

            // Skylight
            val hasSkyLight = dis.readBoolean()
            val skyLight = if (hasSkyLight) {
                dis.readNBytes(2048)
            } else ByteArray(2048)

            return IcedSectionData(batch, blockLight, skyLight)
        }

        fun fromChunk(chunk: Chunk, section: Section): IcedSectionData {
            val blockLight = section.blockLight

            val batch = RelativeBlockBatch()

            for (y in 0 until 16) {
                for (z in 0 until 16) {
                    for (x in 0 until 16) {
                        batch.setBlock(x, y, z, chunk.getBlock(x, y, z))
                    }
                }
            }

            val skyLight = section.skyLight

            return IcedSectionData(batch, blockLight, skyLight)
        }

    }

    fun save(dos: DataOutputStream) {

    }

}