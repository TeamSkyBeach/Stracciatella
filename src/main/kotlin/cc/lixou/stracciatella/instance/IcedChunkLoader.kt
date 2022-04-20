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

    override fun loadInstance(instance: Instance) {
        if(!Files.exists(path)) { return }
        DataInputStream(FileInputStream(path.toFile())).use { dis ->
            println("MinX: ${dis.readShort()}") // works - -4
            println("MinZ: ${dis.readShort()}") // works - -5
            println("SizeX: ${dis.readUnsignedShort()}") // works - 5
            println("SizeZ: ${dis.readUnsignedShort()}") // works - 13
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
            dos.writeShort(-4)
            dos.writeShort(-5)
            dos.writeShort(5)
            dos.writeShort(13)
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        LOGGER.info("Saving chunk ${chunk.chunkX} - ${chunk.chunkZ}")
        return CompletableFuture.completedFuture(null)
    }

}