package cc.lixou.stracciatella.instance

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.Instance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    override fun loadChunk(instance: Instance, chunkX: Int, chunkZ: Int): CompletableFuture<Chunk?> {
        LOGGER.debug("Loading chunk $chunkX - $chunkZ")
        if (!Files.exists(path)) {
            // Whole .iced file not found - new world
            return CompletableFuture.completedFuture(null)
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }

}