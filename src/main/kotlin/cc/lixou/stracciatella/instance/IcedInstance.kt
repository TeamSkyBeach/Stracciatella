package cc.lixou.stracciatella.instance

import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType
import java.util.*

/**
 * Instance Container for our ICED Instance Format
 */
class IcedInstance(uuid: UUID, dimensionType: DimensionType = DimensionType.OVERWORLD, loader: IChunkLoader? = null) : InstanceContainer(uuid, dimensionType, loader) {

}