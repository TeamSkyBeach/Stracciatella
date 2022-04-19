package cc.lixou.stracciatella.instance

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.Instance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

class IcedChunkLoader: IChunkLoader {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(IcedChunkLoader::class.java)
    }

    override fun loadChunk(instance: Instance, chunkX: Int, chunkZ: Int): CompletableFuture<Chunk?> {
        LOGGER.debug("Loading chunk $chunkX - $chunkZ")
        TODO("Not yet implemented")
    }

    override fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }

}