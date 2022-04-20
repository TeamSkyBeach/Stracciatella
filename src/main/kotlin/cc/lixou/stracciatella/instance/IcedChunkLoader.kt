package cc.lixou.stracciatella.instance

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
import java.util.concurrent.CompletableFuture

class IcedChunkLoader(
    private val worldFileName: String
) : IChunkLoader {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(IcedChunkLoader::class.java)
    }

    private var path: Path = Path.of(worldFileName)
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
            dos.writeShort(minX.toInt())
            dos.writeShort(minZ.toInt())
            dos.writeShort(sizeX.toInt())
            dos.writeShort(sizeZ.toInt())
        }
        return CompletableFuture.completedFuture(null)
    }

    /**
     * Prepares the Chunk for saving in an [IcedChunkData]
     */
    override fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        LOGGER.info("Saving chunk ${chunk.chunkX} - ${chunk.chunkZ}")
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
        return CompletableFuture.completedFuture(null)
    }

}