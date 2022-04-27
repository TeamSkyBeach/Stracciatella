package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.data.IcedChunkDataOLD
import cc.lixou.stracciatella.instance.data.IcedSectionDataOLD
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.Instance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CompletableFuture

class IcedChunkLoader(
    private val worldFileName: String
) : IChunkLoader {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(IcedChunkLoader::class.java)
    }

    private var path: Path = Path.of(worldFileName)
    private val chunkData = HashMap<UUID, IcedChunkDataOLD>()

    private var minX: Short = 0
    private var minZ: Short = 0
    private var sizeX: UShort = 0u
    private var sizeZ: UShort = 0u

    override fun loadInstance(instance: Instance) {
        if (!Files.exists(path)) {
            return
        }
        DataInputStream(FileInputStream(path.toFile())).use { dis ->
            minX = dis.readShort()
            minZ = dis.readShort()
            sizeX = dis.readUnsignedShort().toUShort()
            sizeZ = dis.readUnsignedShort().toUShort()
            // TODO: Read chunkMask BitSet
        }
    }

    override fun loadChunk(instance: Instance, chunkX: Int, chunkZ: Int): CompletableFuture<Chunk?> {
        LOGGER.info("Loading chunk $chunkX - $chunkZ")
        if (!Files.exists(path)) {
            // Whole .iced file not found - new world
            return CompletableFuture.completedFuture(null)
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun saveInstance(instance: Instance): CompletableFuture<Void> {
        DataOutputStream(FileOutputStream(path.toFile())).use { dos ->
            /* World Data */
            dos.writeShort(minX.toInt())
            dos.writeShort(minZ.toInt())
            dos.writeShort(sizeX.toInt())
            dos.writeShort(sizeZ.toInt())
            /* Chunk Data (summary) */
            // TODO: BitSet with empty Chunks
            /* Chunk Data (single) */
            chunkData.values.forEach { it.write(dos) }
        }
        return CompletableFuture.completedFuture(null)
    }

    /**
     * Prepares the Chunk for saving in an [IcedChunkDataOLD]
     */
    override fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        LOGGER.info("Saving chunk ${chunk.chunkX} - ${chunk.chunkZ}")
        //chunk.sections.forEach { LOGGER.error(it.toString()) }
        if (chunk.chunkX < minX) {
            minX = chunk.chunkX.toShort()
        } else if (chunk.chunkX > (sizeX + minX.toUInt()).toInt()) {
            sizeX = (chunk.chunkX - minX.toInt()).toUShort()
        }
        if (chunk.chunkZ < minZ) {
            minZ = chunk.chunkZ.toShort()
        } else if (chunk.chunkZ > (sizeZ + minZ.toUInt()).toInt()) {
            sizeZ = (chunk.chunkZ - minZ.toInt()).toUShort()
        }

        val list = ArrayList<IcedSectionDataOLD>()
        for (i in chunk.minSection..chunk.maxSection) {
            list.add(IcedSectionDataOLD(i.toByte()))
        }

        var isEmpty = true
        for (section in chunk.sections) {
            if (section.blockPalette().count() > 0) {
                isEmpty = false
                break
            }
        }

        chunkData[chunk.identifier] = IcedChunkDataOLD(isEmpty, list.toTypedArray())

        return CompletableFuture.completedFuture(null)
    }

    override fun supportsParallelLoading(): Boolean {
        return true
    }

    override fun supportsParallelSaving(): Boolean {
        return true
    }

}