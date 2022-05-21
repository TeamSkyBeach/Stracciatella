package cc.lixou.stracciatella.instance.data

import cc.lixou.stracciatella.instance.LixousBatch
import cc.lixou.stracciatella.instance.util.NBTUtils
import cc.lixou.stracciatella.instance.util.PasteModifier
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.BlockHandler
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mca.unpack
import org.jglrxavpok.hephaistos.nbt.*
import world.cepi.kstom.Manager
import java.io.DataInputStream
import java.io.DataOutputStream

class IcedSectionData(
    val blocks: LixousBatch,
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
                val nbtCompound = NBTUtils.readNBTTag<NBTCompound>(nbtRaw)
                paletteList.add(nbtCompound)
            }

            // Block States
            val blockStatesLength = dis.readInt()
            val compactedBlockStates = ImmutableLongArray(blockStatesLength) { dis.readLong() }

            val sizeInBits = compactedBlockStates.size * 64 / 4096
            val blockStates = unpack(compactedBlockStates, sizeInBits).sliceArray(0 until 4096)

            val blocks = LixousBatch(16, 16, 16)

            for (x in 0 until 16) {
                for (y in 0 until 16) {
                    for (z in 0 until 16) {
                        val pos = x * 16 * 16 + y * 16 + z
                        val value = paletteList[blockStates[pos]]
                        val block = NBTUtils.getBlockFromCompound(value) ?: continue
                        blocks[x, y, z] = block
                    }
                }
            }

            // Tile Entities
            @Suppress("unchecked_cast")
            val tileEntites = NBTReader(dis, CompressedProcesser.NONE).read() as NBTList<NBTCompound>
            tileEntites.forEach { nbtCompound ->
                val x = nbtCompound.getByte("x")!!
                val y = nbtCompound.getByte("y")!!
                val z = nbtCompound.getByte("z")!!
                val id = nbtCompound.getString("id")

                var handler: BlockHandler? = null
                if (id != null) {
                    handler = Manager.block.getHandlerOrDummy(id)
                }

                val mutableCompound = nbtCompound.toMutableCompound()
                mutableCompound.remove("x")
                mutableCompound.remove("y")
                mutableCompound.remove("z")
                mutableCompound.remove("id")

                blocks.setData(x, y, z, mutableCompound.toCompound(), handler)
            }

            // Skylight
            val hasSkyLight = dis.readBoolean()
            val skyLight = if (hasSkyLight) {
                dis.readNBytes(2048)
            } else ByteArray(2048)

            return IcedSectionData(blocks, blockLight, skyLight)
        }

        fun fromChunk(chunk: Chunk, sectionIndex: Int, section: Section): IcedSectionData {
            val blockLight = section.blockLight

            val blocks = LixousBatch(16, 16, 16)

            val sectionPos = sectionIndex * 16

            for (x in 0 until 16) {
                for (y in 0 until 16) {
                    for (z in 0 until 16) {
                        val block = chunk.getBlock(x, y + sectionPos, z)
                        blocks[x, y, z] = block
                        if (block.nbt() != null || block.handler() != null) {
                            val compound = block.nbt()!!.toMutableCompound()
                            compound.setByte("x", x.toByte())
                            compound.setByte("y", y.toByte())
                            compound.setByte("z", z.toByte())
                            if (block.handler() != null) {
                                compound.setString("id", block.handler()!!.namespaceId.asString())
                            }
                            blocks.setData(x, y, z, compound.toCompound(), block.handler())
                        }
                    }
                }
            }

            val skyLight = section.skyLight

            return IcedSectionData(blocks, blockLight, skyLight)
        }

    }

    fun save(dos: DataOutputStream) {
        // Light Data
        dos.writeBoolean(false)
        //dos.write(blockLight)
        //println(blockLight.size) // <- this is 0

        // Palette Data
        dos.writeInt(blocks.palette.elements.size)
        blocks.palette.elements.forEach {
            val serialized = NBTUtils.writeNBTTag(it.toNBT())
            dos.writeInt(serialized.size)
            dos.write(serialized)
        }

        // Block States
        val states = NBT.LongArray(blocks.palette.compactIDs(blocks.blocks))
        states.writeContents(dos)

        // Tile Entities
        val list = mutableListOf<NBTCompound>()
        blocks.datas.forEach { (vec, data) ->
            if (data.first == null && data.second.isEmpty()) return@forEach
            val compound = data.second.toMutableCompound()
            compound.setByte("x", vec.blockX().toByte())
            compound.setByte("y", vec.blockY().toByte())
            compound.setByte("z", vec.blockZ().toByte())
            if (data.first != null) {
                compound.setString("id", data.first!!.namespaceId.asString())
            }
            list.add(compound.toCompound())
        }
        NBTWriter(dos, CompressedProcesser.NONE).writeNamed("", NBT.List(NBTType.TAG_Compound, list))

        // Skylight
        dos.writeBoolean(false)
        //dos.write(skyLight)
    }

    fun paste(instance: Instance, pos: Point, modifier: PasteModifier = PasteModifier()) {
        blocks.paste(instance, pos, modifier)
    }

}